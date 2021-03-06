package outbox.template

import org.springframework.transaction.annotation.Transactional
import outbox.ServiceUtil
import outbox.member.Member

/**
 * @author Ruslan Khmelyuk
 */
class TemplateService {

    static transactional = true

    /**
     * Adds new template.
     *
     * @param template the template to add.
     * @return {@code true} if added, otherwise {@code false}.
     */
    @Transactional
    boolean addTemplate(Template template) {
        ServiceUtil.saveOrRollback(template)
    }

    /**
     * Saves template.
     *
     * @param template the template to save.
     * @return {@code true} if saved, otherwise {@code false}.
     */
    @Transactional
    boolean saveTemplate(Template template) {
        ServiceUtil.saveOrRollback template
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
    List<Template> getMemberTemplates(Member member, int page = 0, int max = 0) {
        if (max) {
            return Template.findAllByOwner(member, [max: max, offset: (page - 1) * max])
        }
        return Template.findAllByOwner(member)
    }

}
