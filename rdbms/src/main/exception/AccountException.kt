package exception

import java.sql.SQLException

class AccountException(message: String?) : SQLException(message)