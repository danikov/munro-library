package com.xdesign.example.demo

import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Shared
import spock.lang.Specification

@SpringBootTest(classes = DemoApplication.class)
class SummitControllerTest extends Specification {
    @Shared
    def client = new RESTClient("http://localhost:8080")

    def 'should return 200 code when fetching all summits'() {
        when: 'fetch all summits'
        def response = client.get(path: '/summit')

        then: 'server returns 200 code (ok)'

        assert response.status == 200
        assert response.contentType == 'application/hal+json'
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit.size == 20
            with(page) {
                assert size == 20
                assert totalElements == 509
                assert totalPages == 26
                assert number == 0
            }
        }
    }

    def 'should filter TOP summits'() {
        when: 'fetch TOP summits'
        def response = client.get(
                path: '/summit',
                query: [category: 'TOP']
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            with(page) {
                assert size == 20
                assert totalElements == 227
                assert totalPages == 12
            }
        }
    }

    def 'should filter MUN summits'() {
        when: 'fetch MUN summits'
        def response = client.get(
                path: '/summit',
                query: [category: 'MUN']
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            with(page) {
                assert size == 20
                assert totalElements == 282
                assert totalPages == 15
            }
        }
    }

    def 'should order by height, ascending'() {
        when: 'fetch ordered by height'
        def response = client.get(
                path: '/summit',
                query: [sort: 'height,asc', size: 10]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            // using Sets as order is unstable
            assert _embedded.summit*.name as Set == [
                    "Beinn Teallach",
                    "Mullach Coire nan Cisteachan [Carn na Caim South Top]",
                    "Sgurr nan Ceathreamhnan - Stob Coire na Cloiche",
                    "Spidean a' Choire Leith (Liathach) - Stuc a' Choire Dhuibh Bhig",
                    "Mullach Coire Mhic Fhearchair - Sgurr Dubh",
                    "Carn Aosda",
                    "Ben Vane",
                    "Carn Liath - Stob Coire Dubh",
                    "Beinn a' Chleibh",
                    "Carn Eige - Stob Coire Lochan"] as Set
        }
    }

    def 'should order by height, descending'() {
        when: 'fetch ordered by height'
        def response = client.get(
                path: '/summit',
                query: [sort: 'height,desc', size: 10]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name as Set == [
                    "Ben Nevis",
                    "Ben Macdui [Beinn Macduibh]",
                    "Braeriach",
                    "Cairn Toul",
                    "Braeriach - Carn na Criche",
                    "Sgor an Lochain Uaine",
                    "Cairn Gorm",
                    "Aonach Beag",
                    "Ben Nevis - Carn Dearg NW Top (new GR)",
                    "Aonach Mor"] as Set
        }
    }

    def 'should order alphabetically, ascending'() {
        when: 'fetch ordered alphabetically'
        def response = client.get(
                path: '/summit',
                query: [sort: 'name,asc', size: 10]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "A' Bhuidheanach Bheag",
                    "A' Bhuidheanach Bheag - Glas Mheall Mor",
                    "A' Chailleach",
                    "A' Chailleach",
                    "A' Chailleach - Toman Coinich [Toman Coinnich]",
                    "A' Chraileag [A' Chralaig]",
                    "A' Chralaig - A' Chioch",
                    "A' Chralaig - Stob Coire na Craileig [Stob Coire na Cralaig] [Mullach Fraoch-choire S Top]",
                    "A' Ghlas-bheinn",
                    "A' Mhaighdean"]
        }
    }

    def 'should order alphabetically, descending'() {
        when: 'fetch ordered alphabetically'
        def response = client.get(
                path: '/summit',
                query: [sort: 'name,desc', size: 10]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "Tom a' Choinich - Tom a' Choinich Beag",
                    "Tom a' Choinich - An Leth-chreag",
                    "Tom a' Choinich",
                    "Tom Buidhe",
                    "Tolmount - Crow Craigies",
                    "Tolmount",
                    "Toll Creagach West Top",
                    "Toll Creagach",
                    "The Saddle - Spidean Dhomhuill Bhric [1891: Sgurr na Creige]",
                    "The Saddle - Sgurr na Forcan"]
        }
    }

    def 'should order by height, ascending, then alphabetically, descending'() {
        when: 'fetch ordered'
        def response = client.get(
                path: '/summit',
                query: [sort: ['height,asc', 'name,desc'], size: 10]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "Mullach Coire nan Cisteachan [Carn na Caim South Top]",
                    "Beinn Teallach",
                    "Spidean a' Choire Leith (Liathach) - Stuc a' Choire Dhuibh Bhig",
                    "Sgurr nan Ceathreamhnan - Stob Coire na Cloiche",
                    "Mullach Coire Mhic Fhearchair - Sgurr Dubh",
                    "Carn Aosda",
                    "Ben Vane",
                    "Carn Liath - Stob Coire Dubh",
                    "Beinn a' Chleibh",
                    "Carn Eige - Stob Coire Lochan"]
        }
    }

    def 'should be able to control page size and paging'() {
        when: 'fetch ordered'
        def response = client.get(
                path: '/summit',
                query: [sort: ['name,asc'], size: 2, page: page]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == names
        }

        where:
        page | names
        0    | ["A' Bhuidheanach Bheag", "A' Bhuidheanach Bheag - Glas Mheall Mor"]
        1    | ["A' Chailleach", "A' Chailleach"]
        2    | ["A' Chailleach - Toman Coinich [Toman Coinnich]", "A' Chraileag [A' Chralaig]"]
        3    | ["A' Chralaig - A' Chioch", "A' Chralaig - Stob Coire na Craileig [Stob Coire na Cralaig] [Mullach Fraoch-choire S Top]"]
        4    | ["A' Ghlas-bheinn", "A' Mhaighdean"]
    }

    def 'should be able to select by minimum height'() {
        when: 'fetch summits with min height'
        def response = client.get(
                path: '/summit',
                query: [sort: 'height,asc', minHeight: 1154.0, size: 5]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "Derry Cairngorm",
                    "Lochnagar - Cac Carn Beag",
                    "Beinn Bhrotain",
                    "Stob Binnein",
                    "Ben Avon - Leabaidh an Daimh Bhuidhe"
            ]
        }
    }

    def 'should be able to select by maximum height'() {
        when: 'fetch summits with min height'
        def response = client.get(
                path: '/summit',
                query: [sort: 'height,desc', maxHeight: 1172.0, size: 5]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "Ben Avon - Leabaidh an Daimh Bhuidhe",
                    "Stob Binnein",
                    "Beinn Bhrotain",
                    "Lochnagar - Cac Carn Beag",
                    "Derry Cairngorm"
            ]
        }
    }

    def 'should be able to select by minimum and maximum height'() {
        when: 'fetch summits with height limits'
        def response = client.get(
                path: '/summit',
                query: [sort: 'height,asc', minHeight: 1155.5, maxHeight: 1170.0, size: 5]
        )

        then:
        assert response.status == 200
        new JsonSlurper().parse(response.data).with {
            assert _embedded.summit*.name == [
                    "Lochnagar - Cac Carn Beag",
                    "Beinn Bhrotain",
                    "Stob Binnein"
            ]
        }
    }

    def 'should return 400 (bad request) code when max height is less than min height'() {
        given:
        client.handler.failure = { resp -> resp }

        when: 'fetch summits with max height less than min height'
        def response = client.get(
                path: '/summit',
                query: [maxHeight: 99.0, minHeight: 100.0]
        )

        then: 'server returns 400 code (bad request)'
        assert response.status == 400
    }
}