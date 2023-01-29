insert into public.person (user_name, password, user_roles) values ('guest', '$2a$10$Qf0aZto9HYow0T3qzhQp8.mc7vuDKNb9YtTpEduagkOm0WKzwI5a6', null); -- pass000
insert into public.person (user_name, password, user_roles) values ('reader', '$2a$10$Ta//rTbMbLCmKzlmgWzFTeag1XkTNJ04rH2yGcIgGy7osZ.0EK7FC', 'ROLE_READER'); -- pass123
insert into public.person (user_name, password, user_roles) values ('writer', '$2a$10$vBmkOvAZPe6c55GFe4bDpuxLcU2VnvXi0fdUoQOTkw1uDpCRUCofS', 'ROLE_READER,ROLE_WRITER'); -- pass456
insert into public.person (user_name, password, user_roles) values ('admin', '$2a$10$XJkpnk4vopmjkUkqTBLe5eRRBzMXOJSMOAXPI9zHxVojsU2ns4SNy', 'ROLE_ADMIN,ROLE_API'); -- pass789
