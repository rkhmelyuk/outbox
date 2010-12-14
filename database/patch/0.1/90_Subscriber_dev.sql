insert into Subscriber(SubscriberId, FirstName, LastName, Email, Enabled, MemberId, GenderId, LanguageId, TimezoneId, CreateDate) values
('02c8e61f557d1cdcfc5633389f084ac3c8b31091', 'Mika', 'Newton', 'test+mika@mailsight.com', true, 3, null, null, null, '2010-09-03'),
('0d1a75b803d3c3baa59e09d9338d69eed95f73ab', 'Jacob', 'Fane', 'test+jacob@mailsight.com', true, 3, 1, null, null, '2010-09-03'),
('02eaa826b9584bf0647bfb2fc4a6c6151a98da09', 'Artur', 'Sir', 'test+artur@mailsight.com', true, 3, 1, null, null, '2010-09-03'),
('a35679cdbc0ea0db8d1cd8e96654bd4f2b9400a3', 'May', 'Dacen', 'test+may@mailsight.com', false, 3, 2, null, null, '2010-09-03');

insert into SubscriptionList(SubscriptionListId, MemberId, Name, Description, Archived, SubscribersNumber, CreateDate) values
(1, 3, 'Clients', 'Direct application clients', false, 2, '2010-09-11 21:11'),
(2, 3, 'Valued Clients', 'Our best clients',  false, 3, '2010-09-11 21:11'),
(3, 3, 'Demo Clients', 'Clients that tried demo', false,  4, '2010-09-11 21:11'),
(4, 3, 'Bonus Clients', 'List of clients that will receive emails with bonuses and promotional codes.', false, 3, '2010-09-11 21:11');

insert into Subscription(SubscriberId, SubscriptionListId, SubscriptionStatusId, CreateDate) values
('02c8e61f557d1cdcfc5633389f084ac3c8b31091', 1, 1, '2010-12-14'),
('02c8e61f557d1cdcfc5633389f084ac3c8b31091', 2, 1, '2010-12-14'),
('02c8e61f557d1cdcfc5633389f084ac3c8b31091', 3, 2, '2010-12-14'),
('0d1a75b803d3c3baa59e09d9338d69eed95f73ab', 3, 1, '2010-12-14'),
('0d1a75b803d3c3baa59e09d9338d69eed95f73ab', 4, 1, '2010-12-14'),
('02eaa826b9584bf0647bfb2fc4a6c6151a98da09', 2, 1, '2010-12-14'),
('02eaa826b9584bf0647bfb2fc4a6c6151a98da09', 3, 1, '2010-12-14'),
('02eaa826b9584bf0647bfb2fc4a6c6151a98da09', 4, 1, '2010-12-14'),
('a35679cdbc0ea0db8d1cd8e96654bd4f2b9400a3', 1, 1, '2010-12-14'),
('a35679cdbc0ea0db8d1cd8e96654bd4f2b9400a3', 2, 2, '2010-12-14'),
('a35679cdbc0ea0db8d1cd8e96654bd4f2b9400a3', 3, 2, '2010-12-14'),
('a35679cdbc0ea0db8d1cd8e96654bd4f2b9400a3', 4, 2, '2010-12-14');