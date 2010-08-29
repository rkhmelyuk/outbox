package outbox

/**
 * @author Ruslan Khmelyuk
 */
class PaginationTagLib {

    def formPagination = { attrs ->
        def name = attrs.name
        def total = attrs.total
        def max = attrs.max
        def maxsteps = attrs.maxsteps?.toInteger()
        def page = attrs.page?.toInteger()
        def callbackClass = attrs.callbackClass
        def currentClass = attrs.currentClass
        int maxPage = Math.ceil((float) total / max)

        if (total <= max) {
            page = 1
        }
        out << "<input type='hidden' id='$name' name='$name' value='$page'/>"
        out << "<script type='text/javascript'>function _$name(page) {\$('#$name').val(page); }</script>"
        if (total > max) {
            out << "<div class='pagination'>"
            if (page != 1) {
                out << "\n<a href='javascript:void(0)' onclick='_$name(${page-1})' class='$callbackClass'>&#9668;</a>"
            }
            if (page - maxsteps > 0) {
                out << "\n<a href='javascript:void(0)' onclick='_$name(1)' class='$callbackClass ${page == 1 ? currentClass : ''}'>1</a>"
                out << "\n..."
            }
            def half = maxsteps / 2
            (-half..half).each {
                def thisPage = page + it
                if (thisPage > 0 && thisPage <= maxPage) {
                    out << "\n<a href='javascript:void(0)' onclick='_$name(${thisPage})' class='$callbackClass ${page == thisPage ? currentClass : ''}'>${page+it}</a>"
                }
            }
            if (maxPage - page > maxsteps) {
                out << "\n..."
                out << "\n<a href='javascript:void(0)' onclick='_$name(${maxPage-1})' class='$callbackClass ${maxPage-1 == page ? currentClass : ''}'>${message(code: 'first')}</a>"
                out << "\n<a href='javascript:void(0)' onclick='_$name($maxPage)' class='$callbackClass ${maxPage == page ? currentClass : ''}'>${message(code: 'first')}</a>"
            }
            if (page + 1 <= maxPage) {
                out << "\n<a href='javascript:void(0)' onclick='_$name(${page+1})' class='$callbackClass'>&#9658;</a>"
            }
            out << "</div>"
        }
    }
}
