package enteties

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class PersonalData(
    @Column(name = "�������", length = 10)
    var passport: String,
    var snils: String
)
