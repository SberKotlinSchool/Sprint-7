package io.vorotov.orm.dao

import io.vorotov.orm.entity.Message
import io.vorotov.orm.entity.Portfolio
import io.vorotov.orm.entity.User

object messageDao : GenericDao<Message, Long>(Message::class.java)

object userDao : GenericDao<User, Long>(User::class.java)

object portfolioDao : GenericDao<Portfolio, Long>(Portfolio::class.java)