package outbox.template

import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class TemplateService {

    static transactional = true

    @Transactional
    private boolean saveOrRollback(Object item) {
        if (!item) {
            return false
        }

        boolean saved = (item.save(flush: true) != null)
        if (!saved) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        }
        return saved
    }

    /**
     * Adds new template.
     *
     * @param template the template to add.
     * @return {@code true} if added, otherwise {@code false}.
     */
    @Transactional
    boolean addTemplate(Template template) {
        saveOrRollback(template)
    }

    /**
     * Saves template.
     *
     * @param template the template to save.
     * @return {@code true} if saved, otherwise {@code false}.
     */
    @Transactional
    boolean saveTemplate(Template template) {
        saveOrRollback template
    }

    /**
     * Deletes template if possible or mark as removed..
     *
     * @param template the template to delete.
     */
    @Transactional
    void deleteTemplate(Template template) {
        if (template && template.id) {
            template.delete()
        }
    }

    /**
     * Gets template by it's id.
     *
     * @param id the template id.
     * @return the found template id.
     */
    @Transactional(readOnly = true)
    Template getTemplate(Long id) {
        Template.get(id)
    }

    /**
     * Gets member templates.
     *
     * @param member the member to get templates for.
     * @return the list of found templates.
     */
    @Transactional(readOnly = true)
    List<Template> getMemberTemplates(Member member, int page, int max) {
        Template.findAllByOwner member, [max: max, offset: (page - 1) * max] 
    }

}
