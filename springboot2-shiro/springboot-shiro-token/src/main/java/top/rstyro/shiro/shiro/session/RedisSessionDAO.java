package top.rstyro.shiro.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * redis session DAO
 */
public class RedisSessionDAO extends CachingSessionDAO {


    @Autowired
    @Qualifier("shiroRedisTemplate")
    private RedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "shiro_redis_session";

    private Logger log = LoggerFactory.getLogger(RedisSessionDAO.class);


    @Override
    protected void doUpdate(Session session) {
        System.out.println("==doUpdate====");
        this.saveSession(session);
    }

    @Override
    protected void doDelete(Session session) {
        System.out.println("==doDelete====");
        if(session ==null || session.getId() == null){
            log.error("session is null");
            return;
        }
        redisTemplate.boundHashOps(KEY_PREFIX).delete(session.getId());
    }

    @Override
    protected Serializable doCreate(Session session) {
        System.out.println("==doCreate====");
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session,sessionId);
        this.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("==doReadSession====");
        if(sessionId ==null){
            log.error("session id is null");
            return null;
        }
        return (Session) redisTemplate.boundHashOps(KEY_PREFIX).get(sessionId);
    }

    private void saveSession(Session session){
        System.out.println("==saveSession====");
        if(session ==null || session.getId() == null){
            log.error("session is null");
            return;
        }
        redisTemplate.boundHashOps(KEY_PREFIX).put(session.getId(),session);
    }

    @Override
    public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {
        super.setSessionIdGenerator(sessionIdGenerator);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        List values = redisTemplate.boundHashOps(KEY_PREFIX).values();
        return Collections.unmodifiableList(values);
    }
}
