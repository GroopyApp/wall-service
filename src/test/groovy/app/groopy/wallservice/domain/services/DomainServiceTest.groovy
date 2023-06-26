package app.groopy.wallservice.domain.services

import app.groopy.wallservice.application.mapper.ApplicationMapper
import app.groopy.wallservice.application.mapper.ApplicationMapperImpl
import app.groopy.wallservice.infrastructure.repository.EventRepository
import app.groopy.wallservice.infrastructure.repository.TopicRepository
import app.groopy.wallservice.infrastructure.repository.UserRepository
import app.groopy.wallservice.infrastructure.repository.WallRepository
import org.spockframework.spring.SpringBean
import org.springframework.data.mongodb.core.MongoTemplate
import spock.lang.Specification
import spock.lang.Subject

class DomainServiceTest extends Specification {

    @SpringBean
    private final TopicRepository topicRepository = Mock TopicRepository
    @SpringBean
    private final EventRepository eventRepository = Mock EventRepository
    @SpringBean
    private final WallRepository wallRepository = Mock WallRepository
    @SpringBean
    private final UserRepository userRepository = Mock UserRepository
    @SpringBean
    private final MongoTemplate mongoTemplate = Mock MongoTemplate
    @SpringBean
    private final Validator validator = new Validator(mongoTemplate)
    @SpringBean
    private final ApplicationMapper applicationMapper = new ApplicationMapperImpl()

    @Subject
    def testSubject = new DomainService(topicRepository, eventRepository, wallRepository, userRepository, mongoTemplate, validator, applicationMapper)

}
