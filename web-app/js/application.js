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
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
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
                passwordConfirmation: { required: Message['password.confirmation.required'], equalTo: Message['wrong.password.confirmation'] }
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
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
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
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
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
                                document.location = Config['contextPath'] + "/member/list";
                            }
                            else {
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriberCreate: function() {
        $('#namePrefix').focus();
        var validator = $('#subscriberForm').validate({
            rules: {
                email: { required: true, email: true }
            },
            messages: {
                email: { required: Message['subscriber.email.blank'], email: Message['subscriber.email.email.invalid'] }
            }
        });
        $('#addSubscriber').click(function() {
            if (validator.form()) {
                $('#subscriberForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = Config['contextPath'] + "/subscriber";
                            }
                            else {
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                        }
                    }
                });
            }
        });
    },

    subscriberEdit: function() {
        $('#nameTitle').focus();
        var validator = $('#subscriberForm').validate({
            rules: {
                email: { required: true, email: true }
            },
            messages: {
                email: { required: Message['subscriber.email.blank'], email: Message['subscriber.email.email.invalid'] }
            }
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
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                        }
                    }
                });
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
        $('#name').keyup(function(e) { if (e.keyCode == 13) addSubscriberType() });
        $('#addSubscriberType').click(function() { addSubscriberType() });
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
                                $('.type input[type=hidden][value='+id+']').parent().remove();
                            }
                        }
                    }
                });
            }
        });
        $('.name').live('click', function() {
            $(this) // {
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
            var id = parent.parent().children('input[name=id]').val()
            var name = parent.children('input[name=name]').val()
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

    subscribersListCreate: function() {
        $('#name').focus();
        var validator = $('#subscribersListForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 }
            },
            messages: {
                name: { required: Message['subscribersList.name.blank'], maxlength: Message['subscribersList.name.maxSize.exceeded'] },
                description: { maxlength: Message['subscribersList.description.maxSize.exceeded'] }
            }
        });
        $('#addSubscribersList').click(function() {
            if (validator.form()) {
                $('#subscribersListForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                document.location = Config['contextPath'] + "/subscribersList";
                            }
                            else {
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                        }
                    }
                });
            }
        });
    },

    subscribersListEdit: function() {
        $('#name').focus();
        var validator = $('#subscribersListForm').validate({
            rules: {
                name: { required: true, maxlength: 200 },
                description: { maxlength: 1000 }
            },
            messages: {
                name: { required: Message['subscribersList.name.blank'], maxlength: Message['subscribersList.name.maxSize.exceeded'] },
                description: { maxlength: Message['subscribersList.description.maxSize.exceeded'] }
            }
        });
        $('#editSubscribersList').click(function() {
            if (validator.form()) {
                $('#subscribersListForm').ajaxSubmit({
                    dataType: 'json',
                    success: function(response, status) {
                        if (response && status == 'success') {
                            if (response.success) {
                                $('.status').show().text(Message['subscribersList.changed.successfully']);
                            }
                            else {
                                var errors = '';
                                for (var i in response.errors) {
                                    errors += response.errors[i] + '<br/>';
                                }
                                $('.status').show().html(errors);
                            }
                        }
                    }
                });
            }
        });
    },

    subscribersListShow: function() {
        $('#deleteSubscribersList').click(function() {
            $('#removeNotion').slideDown();
        });
        $('#discardDeleteSubscribersList').click(function() {
            $('#removeNotion').slideUp();
        });
    }
};

$(document).ready(function() {
    App.initialize();
});
