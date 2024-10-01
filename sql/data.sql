INSERT INTO public.role
(id, code, created_at, name)
VALUES
    (1, 'ADMIN', now(), 'administrateur'),
    (2, 'USER', now(), 'utilisateur'),
    (3, 'RECEPTIONIST', now(), 'réceptionniste'),
    (4, 'MESSENGER', now(), 'coursier');

INSERT INTO public.location
(id, created_at, name, latitude, longitude)
VALUES
(1, now(), 'EDBM', 0.23, 23.3);

INSERT INTO public.system_user
    (id, phone_number, created_at, email, password, last_name, first_name, status, location_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', '0321093828', now(), 'test@test.com', '$2a$10$8mcUad.31YdHPy2omUd30.YkPCr8njdv.mRXB5Muspc0d8hxxwudO', 'Rakoto', 'Toto', 'ACTIVE', 1);

INSERT INTO public.system_user_roles
(user_id, roles_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', 1);

UPDATE public.role SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = 1;
UPDATE public.system_user SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = '51161df7-a5c1-41a5-87c5-c18cff313349';


INSERT INTO public.system_user
(id, phone_number, created_at, email, password, last_name, first_name, status, location_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313350', '0321093828', now(), 'test2@test.com', '$2a$10$8mcUad.31YdHPy2omUd30.YkPCr8njdv.mRXB5Muspc0d8hxxwudO', 'Rakoto', 'Toto', 'ACTIVE', 1);

INSERT INTO public.system_user_roles
(user_id, roles_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313350', 2);

UPDATE public.role SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349';
UPDATE public.system_user SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349';
UPDATE public.location SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349';
