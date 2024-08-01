INSERT INTO public.role
    (id, code, creation_le, modification_le, nom, suppression_le, creation_par_id, modification_par_id, suppression_par_id)
VALUES
    (1, 'ADMIN', '2024-07-31 21:08:13.000000', null, 'administrateur', null, null, null, null);

INSERT INTO public.utilisateur
    (id, contact, creation_le, email, mot_de_passe, nom, prenom, statut, creation_par_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', '0321093828', '2024-07-31 21:18:11.912663', 'test@test.com', '$2a$10$8mcUad.31YdHPy2omUd30.YkPCr8njdv.mRXB5Muspc0d8hxxwudO', 'Rakoto', 'Toto', 0, null);

INSERT INTO public.utilisateur_role
    (utilisateur_id, role_id)
VALUES
    ('51161df7-a5c1-41a5-87c5-c18cff313349', 1);

UPDATE public.role SET creation_par_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = 1;
UPDATE public.utilisateur SET creation_par_id = '51161df7-a5c1-41a5-87c5-c18cff313349' WHERE id = '51161df7-a5c1-41a5-87c5-c18cff313349';