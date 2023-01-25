package ru.sber.comdata.persistence.entities

import org.hibernate.Hibernate
import javax.persistence.*

@Entity(name = "product")
data class Product(@Id
                   @GeneratedValue
                   var id: Long  = 0,

                   @Enumerated(value = EnumType.STRING)
                   @Column(nullable = false)
                   var type: FrequencyType,

                   @OneToOne(cascade = [CascadeType.ALL])
                   var productGroup: ProductGroup,

                   @Column(nullable = false)
                   var name: String,

                   @ManyToMany(targetEntity = ContractType::class, fetch = FetchType.EAGER)
                   var ctypes: List<ContractType> = ArrayList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Product

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()


    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , type = $type , productGroup = $productGroup , name = $name , " +
                "ctypes = $ctypes )"
    }

}

enum class FrequencyType(ct: String) {
    Year("Ежегодно"),
    Month("Ежемесячно")
}