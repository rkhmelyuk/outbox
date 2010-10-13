package outbox

import grails.converters.JSON

/**
 * @author Ruslan Khmelyuk
 */
class ReportTagLib {

    static namespace = 'r'

    def openedNotOpened = { attrs ->
        def container = attrs.remove('container')
        def opened = attrs.remove('opened')
        def notOpened = attrs.remove('notOpened')

        def data = [["${message(code: 'opened')}", opened],
                ["${message(code: 'notOpened')}", notOpened]]

        data = data as JSON

        out << "Report.openedNotOpened('$container', $data);"
    }

    def opensClicks = { attrs ->
        def container = attrs.remove('container')
        def opens = attrs.remove('opens')
        def clicks = attrs.remove('clicks')
        def period = attrs.remove('period')

        def dates = [] as SortedSet<Date>
        def clicksNum = [0l: 0l] as TreeMap
        def opensNum = [0l: 0l] as TreeMap

        opens.each { entry ->
            def date = entry.date
            dates << date
            opensNum.put(date.time, entry.opens)
        }
        clicks.each { entry ->
            def date = entry.date
            dates << date
            clicksNum.put(date.time, entry.clicks)
        }

        dates.sort()

        def categories = ['_']
        dates.each { date ->
            def time = date.time
            def click = clicksNum[time]
            if (click == null) {
                clicksNum[time] = 0
            }
            def open = opensNum[time]
            if (open == null) {
                opensNum[time] = 0
            }

            categories << date.format('dd EEE, HH:00')
        }

        def data = [[name: "${message(code: 'opens')}", data: opensNum.values() ],
                [name: "${message(code: 'clicks')}", data: clicksNum.values() ]]

        data = data as JSON
        categories = categories as JSON

        out << "Report.opensClicks('$container', $categories, $data);"
    }

}
