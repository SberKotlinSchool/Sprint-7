package io.vorotov.orm.dao

import org.hibernate.Session
import org.hibernate.SessionFactory

object SessionTemplate{

    private var sessionFactory: SessionFactory? = null
    private val threadLocal: ThreadLocal<Session> = ThreadLocal()

    fun init(sessionFactory: SessionFactory) {
        if (this.sessionFactory != null) {
            throw IllegalStateException("SessionCreator is already initialized")
        }
        this.sessionFactory = sessionFactory
    }

    fun runWithoutResult(runnable: (session: Session) -> Unit) = runWithResult(runnable)

    fun <T> runWithResult(callable: (session: Session) -> T) =
        sessionFactory?.let { sessFact ->
            threadLocal.get().let { session ->
                if (session == null) {
                    val session2 = sessFact.openSession()
                    threadLocal.set(session2)
                    session2.use { sess ->
                        sess.beginTransaction()
                        val result = callable(sess)
                        sess.transaction.commit()
                        threadLocal.set(null)
                        result
                    }
                } else {
                    callable(session)
                }
            }
        } ?: throw IllegalStateException("SessionCreator is not initialized")

}