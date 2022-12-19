package sber.repositories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import sber.enteties.Cat
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Repository
interface CatRepository : JpaRepository<Cat, Long> {
    fun findAllById(id: Long): List<Cat>

    fun findByName(name:String): List<Cat>

    @Query("select a from Cat a where a.color = :color1")
    fun findByColor(@Param("color1") color:String): List<Cat>
}