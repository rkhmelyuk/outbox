var Message;

App = {
    initialize: function() {

    },

    editProfile: function () {
        $('#saveProfile').click(function() {
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
            })
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
    }
};


$(document).ready(function() {
    App.initialize();
});
