var Message;
var Config;

App = {
    initialize: function() {

    },

    editProfile: function () {
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
                    },
                    complete: function(xhr, status) {
                        if (status == 'error') {
                            $('.status').show().text('Error happened.');
                        }
                    }
                });
            }
        });
    },

    newPassword: function() {
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
                    },
                    complete: function(xhr, status) {
                        if (status == 'error') {
                            $('.status').show().text('Error happened.');
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
        $('#password').password_strength({container: '.password-strength'});
        var validator = $('#memberForm').validate({
            rules: {
                firstName: { required: true },
                lastName: { required: true },
                email: { required: true, email: true },
                language: { required: true },
                timezone: { required: true },
                password: { minlength: 3 },
                passwordConfirmation: { equalTo: '#password' }
            },
            messages: {
                firstName: { required: Message['member.firstName.blank'] },
                lastName: { required: Message['member.lastName.blank'] },
                email: { required: Message['member.email.blank'], email: Message['member.email.email.invalid'] },
                language: { required: Message['member.language.nullable'] },
                timezone: { required: Message['member.timezone.nullable'] },
                password: { minlength: Message['password.minlength']},
                passwordConfirmation: { equalTo: Message['wrong.password.confirmation'] }
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
                    },
                    complete: function(xhr, status) {
                        if (status == 'error') {
                            $('.status').show().text('Error happened.');
                        }
                    }
                });
            }
        });
    },

    memberCreate: function() {
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
                    },
                    complete: function(xhr, status) {
                        if (status == 'error') {
                            $('.status').show().text('Error happened.');
                        }
                    }
                });
            }
        });
    }
};

$(document).ready(function() {
    App.initialize();
});
