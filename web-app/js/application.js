var Message;
var Config;

var App = {
    initialize: function() {
        $('.ajaxError').ajaxSend(function() {
            $(this).hide().text('');
        });
        $('.ajaxError').ajaxSuccess(function() {
            $(this).hide().text('');
        });
        $('.ajaxError').ajaxError(function() {
            $(this).text('Server Error').show();
        });
    },

    editProfile: function () {
        $('#firstName').focus();
        var validator = $('#profileForm').validate({
            rules: {
                firstName: { required: true },
                lastName: { required: true },
                email: { required: true, email: true },
                language: { required: true },
                timezone: { required: true }
            },
            messages: {
                firstName: { required: Message['member.firstName.blank'] },
                lastName: { required: Message['member.lastName.blank'] },
                email: { required: Message['member.email.blank'], email: Message['member.email.email.invalid'] },
                language: { required: Message['member.language.nullable'] },
                timezone: { required: Message['member.timezone.nullable'] }
            }
        });

        $('#saveProfile').click(function() {
            if (validator.form()) {
                $('#profileForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['profile.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    newPassword: function() {
        $('#oldPassword').focus();
        $('#newPassword').password_strength({container: '.password-strength'});
        var validator = $('#newPasswordForm').validate({
            rules: {
                oldPassword: { required: true },
                newPassword: { required: true, minlength: 3 },
                passwordConfirmation: { required: true, equalTo: '#newPassword' }
            },
            messages: {
                oldPassword: { required: Message['old.password.required'] },
                newPassword: { required: Message['new.password.required'], minlength: Message['password.minlength']},
                passwordConfirmation: { required: Message['password.confirmation.required'],
                    equalTo: Message['wrong.password.confirmation'] }
            }
        });
        $('#savePassword').click(function() {
            if (validator.form()) {
                $('#newPasswordForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['password.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    membersList: function() {
        $("#itemsPerPage").change(function() {
            $('#filterForm').submit();
        });
        $(".page_callback").click(function() {
            $('#filterForm').submit();
        });
    },

    memberEdit: function() {
        $('#firstName').focus();
        $('#password').password_strength({container: '.password-strength'});
        var validator = $('#memberForm').validate({
            rules: {
                firstName: { required: true },
                lastName: { required: true },
                email: { required: true, email: true },
                language: { required: true },
                timezone: { required: true },
                password: { minlength: 3 },
                passwordConfirmation: { equalTo: '#password' },
                role: { required: true }
            },
            messages: {
                firstName: { required: Message['member.firstName.blank'] },
                lastName: { required: Message['member.lastName.blank'] },
                email: { required: Message['member.email.blank'], email: Message['member.email.email.invalid'] },
                language: { required: Message['member.language.nullable'] },
                timezone: { required: Message['member.timezone.nullable'] },
                password: { minlength: Message['password.minlength']},
                passwordConfirmation: { equalTo: Message['wrong.password.confirmation'] },
                role: { required: Message['member.role.nullable'] }
            }
        });

        $('#saveMember').click(function() {
            if (validator.form()) {
                $('#memberForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['member.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    memberCreate: function() {
        $('#username').focus();
        $('#password').password_strength({container: '.password-strength'});

        var validator = $('#memberForm').validate({
            rules: {
                username: { required: true },
                firstName: { required: true },
                lastName: { required: true },
                email: { required: true, email: true },
                language: { required: true },
                timezone: { required: true },
                password: { required: true, minlength: 3 },
                passwordConfirmation: { required: true, equalTo: '#password' }
            },
            messages: {
                username: { required: Message['member.username.blank'] },
                firstName: { required: Message['member.firstName.blank'] },
                lastName: { required: Message['member.lastName.blank'] },
                email: { required: Message['member.email.blank'], email: Message['member.email.email.invalid'] },
                language: { required: Message['member.language.nullable'] },
                timezone: { required: Message['member.timezone.nullable'] },
                password: { required: Message['password.required'], minlength: Message['password.minlength']},
                passwordConfirmation: { required: Message['password.confirmation.required'],
                    equalTo: Message['wrong.password.confirmation'] }
            }
        });

        $('#createMember').click(function() {
            if (validator.form()) {
                $('#memberForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = response.redirectTo;
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriberCreate: function() {
        $('#email').focus();

        $.extend($.validator.messages, {
            required: Message['dynamicField.required'],
            number: Message['dynamicField.number'],
            min: $.validator.format(Message['dynamicField.number.min']),
            max: $.validator.format(Message['dynamicField.number.max'])
        });

        var validator = $('#subscriberForm').validate({
            rules: {
                email: { required: true, email: true }
            },
            messages: {
                email: { required: Message['subscriber.email.blank'], email: Message['subscriber.email.email.invalid'] }
            }
        });

        $('.min').each(function () {
            $(this).rules('add', { min: $(this).attr('min') })
        });
        $('.max').each(function () {
            $(this).rules('add', { max: $(this).attr('max') })
        });

        $('#addSubscriber').click(function() {
            if (validator.form()) {
                $('#subscriberForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = response.redirectTo;
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriberEdit: function() {
        $('#email').focus();

        $.extend($.validator.messages, {
            required: Message['dynamicField.required'],
            number: Message['dynamicField.number'],
            min: $.validator.format(Message['dynamicField.number.min']),
            max: $.validator.format(Message['dynamicField.number.max'])
        });

        var validator = $('#subscriberForm').validate({
            rules: {
                email: { required: true, email: true }
            },
            messages: {
                email: { required: Message['subscriber.email.blank'], email: Message['subscriber.email.email.invalid'] }
            }
        });

        $('.min').each(function () {
            $(this).rules('add', { min: $(this).attr('min') })
        });
        $('.max').each(function () {
            $(this).rules('add', { max: $(this).attr('max') })
        });

        $('#editSubscriber').click(function() {
            if (validator.form()) {
                $('#subscriberForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['subscriber.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    dynamicFields: function() {
        $('a#create, a.edit').fancybox({
            hideOnOverlayClick: false,
            hideOnContentClick: false,
            type: 'ajax',
            onComplete: function() {
                $('#label').focus();
            }
        });
        $('a#removeConfirm').fancybox({
            modal: true,
            type: 'inline'
        });

        var over = true;
        $("#dynamicFields").sortable({
            handle: 'div.handler',
            items: 'div.item',
            cursor: 'move',
            start: function(event, ui) {
                over = true
            },
            update: function(event, ui) {
                if (over) {
                    var field = $(ui.item).attr('id');
                    var after = $(ui.item).prev().attr('id');
                    after = after != undefined ? after : '';

                    $('#reorderForm > #fieldId').val(field);
                    $('#reorderForm > #afterFieldId').val(after);
                    $('#reorderForm').ajaxSubmit();
                }
            }
        });

        var draggable = null;
        $(".trash").droppable({
            drop: function(event, ui) {
                draggable = ui.draggable;
                $(draggable).hide();
                if ($(draggable).hasClass('hiddenField')) {
                    $('#hideField').hide();
                }
                else {
                    $('#hideField').show();
                }
                $("a#removeConfirm").trigger('click');
            },
            out: function(event, ui) {
                over = true;
            },
            over: function(event, ui) {
                over = false;
            }
        });

        $('#cancelRemove').click(function() {
            $(draggable).show();
            $("#dynamicFields").sortable('cancel');
            $.fancybox.close();
        });
        $('#hideField').click(function() {
            $(draggable).show();
            $("#dynamicFields").sortable('cancel');

            var field = $(draggable).attr('id');
            $('#hideForm > #fieldId').val(field);
            $('#hideForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#dynamicFieldsBody').load(response.dynamicFieldsLink);
                            //$(draggable).addClass('hiddenField');
                        }
                    }
                },
                complete: function() {
                    $.fancybox.close();
                }
            });
        });
        $('#removeField').click(function() {
            $(draggable).remove();

            var field = $(draggable).attr('id');
            $('#removeForm > #fieldId').val(field);
            $('#removeForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#dynamicFieldsBody').load(response.dynamicFieldsLink);
                        }
                    }
                },
                complete: function() {
                    $.fancybox.close();
                }
            });
        });
    },

    createEditDynamicField: function() {
        var value = $('#type').val();
        $('.constraint').hide();
        $('.constraint_' + value).show();

        addIntegerValidationRule();
        addLessThanValidationRule();
        var validator = $('#saveForm').validate({
            rules: {
                label: { required: true },
                name: { required: true },
                type: { required: true },
                maxlength: { integer: true, min: 0, max: 9999 },
                min: { number: true, lessThan: '#max' },
                max: { number: true }
            },
            messages: {
                label: { required: Message['dynamicField.label.required'] },
                name: { required: Message['dynamicField.name.required'] },
                type: { required: Message['dynamicField.type.required'] },
                maxlength: { integer: Message['dynamicField.maxlength.integer'],
                    min: Message['dynamicField.maxlength.min'],
                    max: Message['dynamicField.maxlength.max']},
                min: { number: Message['dynamicField.min.number'] },
                max: { number: Message['dynamicField.max.number'] }
            }
        });
        $('#saveForm').data('validator', validator);
        $('#label').live('keyup', function() {
            $('#name').val($(this).val().replace(/\s+/g, '_').replace(/[^\w\d_]/g, '').toLowerCase());
        });
        $('#save').click(function() {
            if (validator.form()) {
                $(this).attr('disabled', 'disabled');
                $('#saveForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $.fancybox.close();
                                $('#dynamicFieldsBody').load(response.dynamicFieldsLink);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    },
                    complete: function() {
                        $(this).removeAttr('disabled');
                    }
                });
            }
        });
        $('#addNewSelectValue').click(function() {
            var value = $('#newSelectValue').val();
            if (value) {
                $('#newSelectValue').val('').focus();
                $('#selectValueItemTemplate').tmpl({value: value}).appendTo('#selectValues');
            }
        });
        $('.removeSelectedValue').click(function() {
            $(this).parent().parent().remove();
        });
        $('#newSelectValue').keyup(function(e) {
            if (e.keyCode == 13) $('#addNewSelectValue').click();
        });
        $('#cancel').click(function() {
            $.fancybox.close();
        });
        $('#type').change(function() {
            var value = $(this).val();
            $('.constraint').hide();
            $('.constraint_' + value).show()
        });

        $('.viewItem .itemName').live('click', function() {
            $('#cancelEditSelectedValue').trigger('click');

            var parent = $(this).parent().hide();
            $('#editSelectValueItemTemplate').tmpl({value: $(this).text()}).insertAfter(parent);
            $('#itemNameLabel').focus();
        });
        $('#saveEditSelectedValue').live('click', function() {
            var value = $('#itemNameLabel').val();
            $('.editItem').siblings('#selectValueLabels').val(value);

            var item = $('.editItem').siblings('.viewItem');
            $(item).children('.itemName').text(value);

            $('.editItem').remove();
            $(item).show();
        });
        $('#cancelEditSelectedValue').live('click', function() {
            var editItem = $('.editItem');
            $(editItem).siblings('.viewItem').show();
            $(editItem).remove();
        });
        $('#itemNameLabel').live('keyup', function(e) {
            if (e.keyCode == 13) {
                $('#saveEditSelectedValue').trigger('click');
                e.stopPropagation();
            }
        });
    },

    subscriberTypes: function() {
        var validator = $('#addSubscriberTypeForm').validate({
            rules: {
                name: { required: true}
            },
            messages: {
                name: { required: Message['subscriberType.name.required'] }
            }
        });
        $('#name').keyup(function(e) {
            if (e.keyCode == 13) addSubscriberType()
        });
        $('#addSubscriberType').click(function() {
            addSubscriberType()
        });
        $('.removeSubscriberType').live('click', function() {
            var id = $(this).parent().parent().children('input[name=id]').val();
            if (id && confirm(Message['subscriberType.remove.confirm'])) {
                $('#id').val(id);
                $('#deleteSubscriberTypeForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (!response.success) {
                                $('.status').show().text(Message['subscriberType.delete.failed']);
                            }
                            else {
                                $('.type input[type=hidden][value=' + id + ']').parent().remove();
                            }
                        }
                    }
                });
            }
        });
        $('.name').live('click', function() {
            $(this)// {
                    .hide().parent()
                    .children('.editType').show()
                    .children('.editNameInput').focus();
            // }
        });
        $('.editNameInput').keyup(function(e) {
            if (e.keyCode == 13) saveSubscriberType(this);
            if (e.keyCode == 27) cancelEditSubscriberType(this);
        });
        $('.cancelEditSubscriberType').live('click', function() {
            cancelEditSubscriberType(this)
        });
        $('.updateSubscriberType').live('click', function() {
            saveSubscriberType(this)
        });
        function addSubscriberType() {
            if (validator.form()) {
                $('#addSubscriberTypeForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (!response.success) {
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                            else {
                                var prototype = $('#typePrototype').html();
                                prototype = prototype.replace(/{{id}}/g, response.subscriberType.id);
                                prototype = prototype.replace(/{{name}}/g, response.subscriberType.name);
                                $('#name').val('');
                                $('#types').append(prototype);
                            }
                        }
                    }
                });
            }
        }

        function cancelEditSubscriberType(elem) {
            var parent = $(elem).parent();
            parent.hide();
            parent.parent().children('.name').show();
        }

        function saveSubscriberType(elem) {
            var parent = $(elem).parent();
            parent.hide();
            parent.parent().children('.name').show();
            var id = parent.parent().children('input[name=id]').val();
            var name = parent.children('input[name=name]').val();
            $('#updateSubscriberTypeId').val(id);
            $('#updateSubscriberTypeName').val(name);
            $('#updateSubscriberTypeForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (!response.success) {
                            $('.status').show().text(Message['subscriberType.delete.failed']);
                        }
                        else {
                            parent.parent().children('.name').text(name);
                        }
                    }
                }
            });
        }
    },

    subscriptionLists: function() {
        var orderColumn = $('#column').val();
        $('.sortColumn').each(function () {
            if ($(this).attr('rel') == orderColumn) {
                $(this).addClass($('#sort').val());
            }
        });

        $('.page_callback').click(function() {
            $('#filterForm').submit();
        });
    },

    archivedSubscriptionLists: function() {
        var orderColumn = $('#column').val();
        $('.sortColumn').each(function () {
            if ($(this).attr('rel') == orderColumn) {
                $(this).addClass($('#sort').val());
            }
        });

        $('.page_callback').click(function() {
            $('#filterForm').submit();
        });
    },

    subscriptionListCreate: function() {
        $('#name').focus();
        var validator = $('#subscriptionListForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 }
            },
            messages: {
                name: { required: Message['subscriptionList.name.blank'], maxlength: Message['subscriptionList.name.maxSize.exceeded'] },
                description: { maxlength: Message['subscriptionList.description.maxSize.exceeded'] }
            }
        });
        $('#addSubscriptionList').click(function() {
            if (validator.form()) {
                $('#subscriptionListForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = response.redirectTo;
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriptionListEdit: function() {
        $('#name').focus();
        var validator = $('#subscriptionListForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 }
            },
            messages: {
                name: { required: Message['subscriptionList.name.blank'], maxlength: Message['subscriptionList.name.maxSize.exceeded'] },
                description: { maxlength: Message['subscriptionList.description.maxSize.exceeded'] }
            }
        });
        $('#editSubscriptionList').click(function() {
            if (validator.form()) {
                $('#subscriptionListForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['subscriptionList.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriptionListShow: function() {
        $('#deleteSubscriptionList').click(function() {
            $('.notion').slideUp();
            $('#removeNotion').slideDown();
        });
        $('#archiveSubscriptionList').click(function() {
            $('.notion').slideUp();
            $('#archiveNotion').slideDown();
        });
        $('#restoreSubscriptionList').click(function() {
            $('.notion').slideUp();
            $('#restoreNotion').slideDown();
        });
        $('.discard').click(function() {
            $('.notion').slideUp();
        });
    },

    templateCreate: function() {
        $('#name').focus();
        var validator = $('#templateForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 },
                templateBody: { required: true }
            },
            messages: {
                name: { required: Message['template.name.blank'], maxlength: Message['template.name.maxSize.exceeded'] },
                description: { maxlength: Message['template.description.maxSize.exceeded'] },
                templateBody: { required: Message['template.templateBody.blank'] }
            }
        });
        $('#addTemplate').click(function() {
            if (validator.form()) {
                $('#templateForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = response.redirectTo;
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    templateEdit: function() {
        $('#name').focus();
        var validator = $('#templateForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 },
                templateBody: { required: true }
            },
            messages: {
                name: { required: Message['template.name.blank'], maxlength: Message['template.name.maxSize.exceeded'] },
                description: { maxlength: Message['template.description.maxSize.exceeded'] },
                templateBody: { required: Message['template.templateBody.blank'] }
            }
        });
        $('#editTemplate').click(function() {
            if (validator.form()) {
                $('#templateForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['template.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    templatesList: function() {
        $('#moreTemplates').click(function() {
            var link = $(this).attr('rel');
            $.ajax({
                url: link,
                success: function(data) {
                    if (data.content) {
                        $('#templates').append(data.content);
                    }
                    if (data.nextPage) {
                        $('#moreTemplates').attr('rel', data.nextPage);
                    }
                    else {
                        $('#moreTemplates').hide()
                    }
                }
            });
        });
    },

    campaignCreate: function() {
        $('#name').focus();
        var validator = $('#campaignForm').validate({
            rules: {
                name: { required: true, maxlength: 500 },
                description: { maxlength: 4000 }
            },
            messages: {
                name: { required: Message['campaign.name.blank'], maxlength: Message['campaign.name.maxSize.exceeded'] },
                description: { maxlength: Message['campaign.description.maxSize.exceeded'] }
            }
        });
        $('#addCampaign').click(function() {
            if (validator.form()) {
                $('#campaignForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = response.redirectTo;
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    campaignEdit: function() {
        $('#name').focus();
        var validator = $('#campaignForm').validate({
            rules: {
                name: { required: true, maxlength: 500 },
                description: { maxlength: 4000 }
            },
            messages: {
                name: { required: Message['campaign.name.blank'], maxlength: Message['campaign.name.maxSize.exceeded'] },
                description: { maxlength: Message['campaign.description.maxSize.exceeded'] }
            }
        });
        $('#updateCampaign').click(function() {
            if (validator.form()) {
                $('#campaignForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('#name').text(response.name);
                                $('.status').show().text(Message['campaign.changed.successfully']);
                            }
                            else {
                                showErrors(response);
                            }
                        }
                    }
                });
            }
        });
    },

    campaignSubscribers: function() {
        $('#subscriptionList').focus();
        $('#addSubscriptionList').live('click', function() {
            $('#addSubscriptionListForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#state').html(response.stateName);
                            $('#notifications').html(response.notifications);
                            $('#actions').html(response.actions);
                            $('#showBody').html(response.content);
                            $('#subscriptionList').focus();
                        }
                        else {
                            showErrors(response);
                        }
                    }
                }
            });
        });
        $('.removeSubscriptionList').live('click', function() {
            $('#campaignSubscriptionId').val($(this).attr('rel'));
            $('#removeSubscriptionListForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#state').html(response.stateName);
                            $('#notifications').html(response.notifications);
                            $('#actions').html(response.actions);
                            $('#showBody').html(response.content);
                            $('#subscriptionList').focus();
                        }
                        else {
                            showErrors(response);
                        }
                    }
                }
            });
        });
    },

    campaignTemplate: function() {
        $('#template').focus();
        $('#useTemplate').live('click', function() {
            $('#useTemplateForm').ajaxSubmit({
                dataType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#state').html(response.stateName);
                            $('#notifications').html(response.notifications);
                            $('#actions').html(response.actions);
                            $('#showBody').html(response.content);
                            $('#template').focus();
                        }
                        else {
                            showErrors(response);
                        }
                    }
                }
            });
        });
    },

    campaignShow: function() {
        $('#sendCampaign').live('click', function() {
            var link = $(this).attr('rel');
            $.ajax({
                url: link,
                dateType: 'json',
                success: function(response, status) {
                    if (response && status == 'success') {
                        if (response.success) {
                            $('#state').html(response.stateName);
                            $('#notifications').html(response.notifications);
                            $('#actions').html(response.actions);
                        }
                        else {
                            showErrors(response);
                        }
                    }
                }
            });
        });
    },

    campaignReports: function() {

    },

    subscriberSearch: function () {
        $('#search, .search_callback').live('click', function() {
            $('#searchForm').ajaxSubmit({
                success: function(response) {
                    $('#searchResult').replaceWith(response);
                }
            });
        });

        $('.number').live('keydown', function(e) {
            var key = e.charCode || e.keyCode || 0;
            return (key == 8 || key == 9 ||
                    key == 46 || key == 109 ||
                    (key >= 37 && key <= 40) ||
                    (key >= 48 && key <= 57) ||
                    (key >= 96 && key <= 105));
        });

        $('#addCondition').live('click', function() {
            $('#search').trigger('click');

            var lastType = $('select[name$=".type"]').last().val();
            if (lastType == undefined) lastType = '';

            var nextRow = $('input[name=row]').last().val();
            if (nextRow == undefined) nextRow = 0;
            nextRow++;

            var url = $('#url').val();
            $.get(url, { type: lastType, row: nextRow}, function(data) {
                $('#conditions').append(data);
                $('#row\\[' + nextRow + '\\]\\.type').focus();
            });
        });
        $('.removeCondition').live('click', function() {
            $(this).parent().parent().remove();
            $('#search').trigger('click');
        });
        $('select[name$=type]').live('change', function() {
            var url = $('#url').val();
            var row = $(this).parent().parent();
            var rowId = $(row).children('input[name=row]').val();
            var type = $('#row\\[' + rowId + '\\]\\.type').val();
            var concatenation = $('#row\\[' + rowId + '\\]\\.concatenation').val();

            $.get(url, { type: type, row: rowId, concatenation: concatenation }, function(data) {
                $(row).replaceWith(data);
                $('#row\\[' + rowId + '\\]\\.field').focus();
            });
        });
        $('select[name$=field]').live('change', function() {
            var url = $('#url').val();
            var row = $(this).parent().parent().parent();
            var rowId = $(row).children('input[name=row]').val();
            var type = $('#row\\[' + rowId + '\\]\\.type').val();
            var field = $('#row\\[' + rowId + '\\]\\.field').val();
            var concatenation = $('#row\\[' + rowId + '\\]\\.concatenation').val();

            var data = { type: type, row: rowId, field: field, concatenation: concatenation };
            $.get(url, data, function(data) {
                $(row).replaceWith(data);
                $('#row\\[' + rowId + '\\]\\.comparison').focus();
            });
        });
        $('select[name$=comparison]').live('change', function() {
            var url = $('#url').val();
            var row = $(this).parent().parent().parent();
            var rowId = $(row).children('input[name=row]').val();
            var type = $('#row\\[' + rowId + '\\]\\.type').val();
            var field = $('#row\\[' + rowId + '\\]\\.field').val();
            var comparison = $('#row\\[' + rowId + '\\]\\.comparison').val();
            var value = $('#row\\[' + rowId + '\\]\\.value').val();
            var concatenation = $('#row\\[' + rowId + '\\]\\.concatenation').val();

            var data = {
                type: type, row: rowId, field: field,
                comparison: comparison, value: value,
                concatenation: concatenation
            };
            $.get(url, data, function(data) {
                $(row).replaceWith(data);
                $('#row\\[' + rowId + '\\]\\.value').focus();
            });
        });
        $('select[name$=subscriptionType]').live('change', function() {
            var url = $('#url').val();
            var row = $(this).parent().parent().parent();
            var rowId = $(row).children('input[name=row]').val();
            var type = $('#row\\[' + rowId + '\\]\\.type').val();
            var subscriptionType = $('#row\\[' + rowId + '\\]\\.subscriptionType').val();
            var subscriptionList = $('#row\\[' + rowId + '\\]\\.subscriptionList').val();
            var concatenation = $('#row\\[' + rowId + '\\]\\.concatenation').val();

            var data = {
                type: type, row: rowId,
                subscriptionType: subscriptionType,
                subscriptionList: subscriptionList,
                concatenation: concatenation
            };
            $.get(url, data, function(data) {
                $(row).replaceWith(data);
                $('#row\\[' + rowId + '\\]\\.subscriptionList').focus();
            });
        });
    }
};

function showErrors(response) {
    var errors = '';
    for (var i in response.errors) {
        errors += response.errors[i] + '<br/>';
    }
    $('.status').show().html(errors);
}

function addIntegerValidationRule() {
    $.validator.addMethod('integer', function(value, element, param) {
        return this.optional(element) || value == parseInt(value, 10);
    }, 'Please enter an integer value.');
}

function addLessThanValidationRule() {
    $.validator.addMethod('lessThan', function(value, element, param) {
        if (this.optional(element)) {
            return true
        }
        var otherValue = parseFloat($(param).val(), 10);
        if (isNaN(otherValue)) {
            return true
        }
        return parseFloat(value, 10) < otherValue;
    }, 'Value is too large.');
}

$(document).ready(function() {
    App.initialize();
});
