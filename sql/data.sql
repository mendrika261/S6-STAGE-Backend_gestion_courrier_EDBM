INSERT INTO public.role
    (id, code, created_at, name)
VALUES
    (1, 'ADMIN', '2024-07-31 21:08:13.000000', 'administrateur');

INSERT INTO public.system_user
    (id, phone_number, created_at, email, password, last_name, first_name, status)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', '0321093828', '2024-07-31 21:18:11.912663', 'test@test.com', '$2a$10$8mcUad.31YdHPy2omUd30.YkPCr8njdv.mRXB5Muspc0d8hxxwudO', 'Rakoto', 'Toto', 'WORKING');

INSERT INTO public.system_user_roles
    (user_id, roles_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', 1);

UPDATE public.role SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = 1;
UPDATE public.system_user SET created_by_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = '51161df7-a5c1-41a5-87c5-c18cff313349';