Created using spring initializr (https://start.spring.io/)

This implements the Munro Library Challenge using Spring's Boot, Rest Data, and HATEOAS modules to rapidly provide sorted & paginated access to entity data.

References used:
- Spring Data: https://spring.io/projects/spring-data
- QueryDSL: http://www.querydsl.com/
- Baeldung: https://www.baeldung.com/
- OpenCSV: http://opencsv.sourceforge.net/

Debugging:
- Getting gradle + IDEA annotation processing to play ball: https://stackoverflow.com/questions/50537096/annotationprocessor-and-dependencies
- Predicate autowiring when using RepositoryRestController: https://jira.spring.io/si/jira.issueviews:issue-html/DATAREST-838/DATAREST-838.html
- Error responses hiding reason/messages (not a production level fix): https://stackoverflow.com/questions/62561211/spring-responsestatusexception-does-not-return-reason

TODO List:
- Prefer to use BigDecimal for heights over Double
- Would be nice to have a pure JSON representation in case people don't like the extra baggage of HATEOAS
- Look into the hack/bug around predicates, maybe contribute back to the project if a good fix can be found