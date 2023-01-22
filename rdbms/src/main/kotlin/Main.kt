import ru.sber.rdbms.TransferConstraint
import java.sql.DriverManager

fun main() {

    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/db",
        "postgres",
        "postgres"
    )

    TransferConstraint(connection).transfer(1,2,1000)
//    TransferOptimisticLock(connection).transfer(2,1,2000)
//    TransferPessimisticLock(connection).transfer(1,2,1000)

}