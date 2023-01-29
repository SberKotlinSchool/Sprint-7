import enteties.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Tank::class.java)
        .addAnnotatedClass(Engine::class.java)
        .addAnnotatedClass(Ammo::class.java)
        .buildSessionFactory()

    sessionFactory.use { sf ->
        val dao = TanksDAO(sf)

        val tank1 = Tank (
            tankType = TankType.T_80
            , engine = Engine(type = EngineType.B_84MS, power = 1100, serialNumber = "125684949")
            , ammo = mutableListOf(
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 2000),
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_12_7, quantity = 300),
                Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_125, quantity = 15),
                Ammo(type = AmmoType.HE, caliber = AmmoCaliber.MM_125, quantity = 21),
                Ammo(type = AmmoType.TUR, caliber = AmmoCaliber.MM_125, quantity = 6),
            )
        )

        val tank2 = Tank (
            tankType = TankType.CHALLENGER_2
            , engine = Engine(type = EngineType.CV12, power = 1200, serialNumber = "RF125Y49")
            , ammo = mutableListOf(
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 4000),
                Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_120, quantity = 12),
                Ammo(type = AmmoType.HEAT, caliber = AmmoCaliber.MM_120, quantity = 20),
                Ammo(type = AmmoType.HESH, caliber = AmmoCaliber.MM_120, quantity = 20),
            )
        )

        // Create
        dao.save(tank1)
        dao.save(tank2)

        // Read
        var found = dao.find(tank1.id)
        println("Найден танк: $found")

        // Update
        tank1.tankType = TankType.T_90
        dao.update(tank1)

        found = dao.find(tank1.id)
        println("Найден обновленный танк: $found")

        // Read a few:

        val tank3 = Tank (
            tankType = TankType.ABRAMS
            , engine = Engine(type = EngineType.AGT_1500, power = 1500, serialNumber = "ARF125Y49")
            , ammo = mutableListOf(
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 11400),
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_12_7, quantity = 1000),
                Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_120, quantity = 10),
                Ammo(type = AmmoType.HEAT, caliber = AmmoCaliber.MM_120, quantity = 10),
                Ammo(type = AmmoType.HESH, caliber = AmmoCaliber.MM_120, quantity = 20),
            )
        )

        val tank4 = Tank (
            tankType = TankType.ABRAMS
            , engine = Engine(type = EngineType.AGT_1500, power = 1500, serialNumber = "SDF125Y49")
            , ammo = mutableListOf(
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_7_62, quantity = 11400),
                Ammo(type = AmmoType.BULLET, caliber = AmmoCaliber.MM_12_7, quantity = 1000),
                Ammo(type = AmmoType.SABOT, caliber = AmmoCaliber.MM_120, quantity = 8),
                Ammo(type = AmmoType.HEAT, caliber = AmmoCaliber.MM_120, quantity = 12),
                Ammo(type = AmmoType.HESH, caliber = AmmoCaliber.MM_120, quantity = 20),
            )
        )

        dao.save(tank3)
        dao.save(tank4)

        val tanks = dao.find(TankType.ABRAMS)
        if (tanks != null) {
            println("Найдены танки:")
            for (tank in tanks) {
                println("$tank")
            }
        }

        // Delete
        val id = tank4.id
        println("Удаляем танк с id = $id")
        dao.delete(id)

        found = dao.find(id)
        if (found != null)
            println("Найден танк: $found")
        else
            println("Танк с id = $id не найден")

    }
}

class TanksDAO(private val sessionFactory: SessionFactory) {
    // Must be CRUD:

    // Create
    fun save(tank: Tank) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(tank)
            session.transaction.commit()
        }
    }

    // Update
    fun update(tank: Tank) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.update(tank)
            session.transaction.commit()
        }
    }


    // Select by ID
    fun find(id: Long): Tank? {
        val result: Tank?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Tank::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    // Select by Type
    fun find(type: TankType): List<Tank>? {
        val result: MutableList<Tank>?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            @Suppress("UNCHECKED_CAST")
            result = session.createQuery("from Tank where tankType = :type").setParameter("type", type).list() as MutableList<Tank>
            session.transaction.commit()
        }
        return result
    }

    // Delete
    fun delete(id: Long) {
        val tank: Tank?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            tank = find(id)
            session.delete(tank)
            session.transaction.commit()
        }
    }
}