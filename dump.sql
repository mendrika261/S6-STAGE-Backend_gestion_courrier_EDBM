PGDMP         7            	    |            courrier    14.13 (Homebrew)    14.13 (Homebrew) t    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    30207    courrier    DATABASE     S   CREATE DATABASE courrier WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'C';
    DROP DATABASE courrier;
                mendrika    false            �            1259    30618    app    TABLE     �  CREATE TABLE public.app (
    id uuid NOT NULL,
    authorized_role_prefix character varying(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255),
    logo_url character varying(255),
    name character varying(255) NOT NULL,
    removed_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    url character varying(255) NOT NULL,
    created_by_id uuid,
    removed_by_id uuid,
    updated_by_id uuid
);
    DROP TABLE public.app;
       public         heap    mendrika    false            �            1259    30625    file    TABLE     9  CREATE TABLE public.file (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    path character varying(255) NOT NULL,
    size bigint NOT NULL,
    type character varying(255) NOT NULL,
    created_by_id uuid NOT NULL,
    files_id bigint
);
    DROP TABLE public.file;
       public         heap    mendrika    false            �            1259    30632    file_history    TABLE     �  CREATE TABLE public.file_history (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    path character varying(255) NOT NULL,
    removed_at timestamp(6) without time zone,
    type character varying(255) NOT NULL,
    created_by_id uuid NOT NULL,
    file_id uuid NOT NULL,
    mail_id uuid NOT NULL,
    removed_by_id uuid
);
     DROP TABLE public.file_history;
       public         heap    mendrika    false            �            1259    30737    file_history_seq    SEQUENCE     z   CREATE SEQUENCE public.file_history_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.file_history_seq;
       public          mendrika    false            �            1259    30639    location    TABLE     �  CREATE TABLE public.location (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    latitude double precision NOT NULL,
    longitude double precision NOT NULL,
    name character varying(255) NOT NULL,
    removed_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    created_by_id uuid,
    removed_by_id uuid,
    updated_by_id uuid
);
    DROP TABLE public.location;
       public         heap    mendrika    false            �            1259    30738    location_seq    SEQUENCE     v   CREATE SEQUENCE public.location_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 #   DROP SEQUENCE public.location_seq;
       public          mendrika    false            �            1259    30644    mail    TABLE     �  CREATE TABLE public.mail (
    id uuid NOT NULL,
    confidentiality character varying(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description oid,
    note_for_messenger oid,
    object character varying(255) NOT NULL,
    priority character varying(255) NOT NULL,
    receiver character varying(255),
    reference character varying(255) NOT NULL,
    sender character varying(255),
    status character varying(255) NOT NULL,
    created_by_id uuid NOT NULL,
    receiver_location_id bigint,
    receiver_user_id uuid,
    sender_location_id bigint,
    sender_user_id uuid,
    CONSTRAINT mail_confidentiality_check CHECK (((confidentiality)::text = ANY ((ARRAY['PUBLIC'::character varying, 'PRIVATE'::character varying, 'SECRET'::character varying])::text[]))),
    CONSTRAINT mail_priority_check CHECK (((priority)::text = ANY ((ARRAY['NORMAL'::character varying, 'URGENT'::character varying])::text[]))),
    CONSTRAINT mail_status_check CHECK (((status)::text = ANY ((ARRAY['CANCELED'::character varying, 'DRAFT'::character varying, 'WAITING'::character varying, 'DELIVERING'::character varying, 'DONE'::character varying])::text[])))
);
    DROP TABLE public.mail;
       public         heap    mendrika    false            �            1259    30654 
   mail_files    TABLE     Z   CREATE TABLE public.mail_files (
    mail_id uuid NOT NULL,
    files_id uuid NOT NULL
);
    DROP TABLE public.mail_files;
       public         heap    mendrika    false            �            1259    30657    mail_history    TABLE     B  CREATE TABLE public.mail_history (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255),
    mail_confidentiality character varying(255) NOT NULL,
    note_for_messenger character varying(255),
    object character varying(255) NOT NULL,
    priority character varying(255) NOT NULL,
    receiver character varying(255) NOT NULL,
    reference character varying(255) NOT NULL,
    removed_at timestamp(6) without time zone,
    sender character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    created_by_id uuid NOT NULL,
    mail_id uuid NOT NULL,
    receiver_location_id bigint NOT NULL,
    removed_by_id uuid,
    sender_location_id bigint NOT NULL,
    CONSTRAINT mail_history_mail_confidentiality_check CHECK (((mail_confidentiality)::text = ANY ((ARRAY['PUBLIC'::character varying, 'PRIVATE'::character varying, 'SECRET'::character varying])::text[]))),
    CONSTRAINT mail_history_priority_check CHECK (((priority)::text = ANY ((ARRAY['NORMAL'::character varying, 'URGENT'::character varying])::text[]))),
    CONSTRAINT mail_history_status_check CHECK (((status)::text = ANY ((ARRAY['CANCELED'::character varying, 'DRAFT'::character varying, 'WAITING'::character varying, 'DELIVERING'::character varying, 'DONE'::character varying])::text[])))
);
     DROP TABLE public.mail_history;
       public         heap    mendrika    false            �            1259    30739    mail_history_seq    SEQUENCE     z   CREATE SEQUENCE public.mail_history_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.mail_history_seq;
       public          mendrika    false            �            1259    30667 	   mouvement    TABLE       CREATE TABLE public.mouvement (
    id bigint NOT NULL,
    coordinates oid,
    description oid,
    end_date timestamp(6) without time zone,
    estimated_delay double precision,
    estimated_distance double precision,
    receiver character varying(255) NOT NULL,
    sender character varying(255) NOT NULL,
    start_date timestamp(6) without time zone NOT NULL,
    status character varying(255) NOT NULL,
    mail_id uuid,
    messenger_id uuid,
    receiver_location_id bigint NOT NULL,
    receiver_user_id uuid,
    sender_location_id bigint NOT NULL,
    sender_user_id uuid,
    CONSTRAINT mouvement_status_check CHECK (((status)::text = ANY ((ARRAY['CANCELLED'::character varying, 'WAITING'::character varying, 'DELIVERING'::character varying, 'DONE'::character varying])::text[])))
);
    DROP TABLE public.mouvement;
       public         heap    mendrika    false            �            1259    30740    mouvement_seq    SEQUENCE     w   CREATE SEQUENCE public.mouvement_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.mouvement_seq;
       public          mendrika    false            �            1259    30675    notification    TABLE     4  CREATE TABLE public.notification (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    description character varying(255) NOT NULL,
    done_at timestamp(6) without time zone,
    title character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    url character varying(255) NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT notification_type_check CHECK (((type)::text = ANY ((ARRAY['DANGER'::character varying, 'WARNING'::character varying, 'INFO'::character varying, 'SUCCESS'::character varying])::text[])))
);
     DROP TABLE public.notification;
       public         heap    mendrika    false            �            1259    30683    role    TABLE     �  CREATE TABLE public.role (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    color character varying(255),
    created_at timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    removed_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    created_by_id uuid,
    removed_by_id uuid,
    updated_by_id uuid
);
    DROP TABLE public.role;
       public         heap    mendrika    false            �            1259    30741    role_seq    SEQUENCE     r   CREATE SEQUENCE public.role_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    DROP SEQUENCE public.role_seq;
       public          mendrika    false            �            1259    30690    session    TABLE     �  CREATE TABLE public.session (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    expired_at timestamp(6) without time zone,
    ip_address character varying(255) NOT NULL,
    last_activity_at timestamp(6) without time zone NOT NULL,
    query_count bigint NOT NULL,
    status character varying(255) NOT NULL,
    token_value character varying(255),
    user_agent character varying(255) NOT NULL,
    user_id uuid,
    CONSTRAINT session_status_check CHECK (((status)::text = ANY ((ARRAY['INTRUSION'::character varying, 'TENTATIVE'::character varying, 'EXPIRED'::character varying, 'ACTIVE'::character varying, 'FINISHED'::character varying])::text[])))
);
    DROP TABLE public.session;
       public         heap    mendrika    false            �            1259    30698    sys_user    TABLE     �  CREATE TABLE public.sys_user (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    phone_number character varying(255),
    status character varying(255) NOT NULL,
    created_by_id uuid,
    location_id bigint NOT NULL,
    CONSTRAINT sys_user_status_check CHECK (((status)::text = ANY ((ARRAY['DISABLE'::character varying, 'SUSPENDED'::character varying, 'CREATED'::character varying, 'PENDING'::character varying, 'ACTIVE'::character varying])::text[])))
);
    DROP TABLE public.sys_user;
       public         heap    mendrika    false            �            1259    30706    sys_user_roles    TABLE     `   CREATE TABLE public.sys_user_roles (
    user_id uuid NOT NULL,
    roles_id bigint NOT NULL
);
 "   DROP TABLE public.sys_user_roles;
       public         heap    mendrika    false            �            1259    30711    user_history    TABLE     �  CREATE TABLE public.user_history (
    id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    email character varying(255),
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    password character varying(255),
    phone_number character varying(255),
    removed_at timestamp(6) without time zone,
    status character varying(255) NOT NULL,
    created_by_id uuid NOT NULL,
    removed_by_id uuid,
    user_id uuid NOT NULL,
    CONSTRAINT user_history_status_check CHECK (((status)::text = ANY ((ARRAY['DISABLE'::character varying, 'SUSPENDED'::character varying, 'CREATED'::character varying, 'PENDING'::character varying, 'ACTIVE'::character varying])::text[])))
);
     DROP TABLE public.user_history;
       public         heap    mendrika    false            �            1259    30742    user_history_seq    SEQUENCE     z   CREATE SEQUENCE public.user_history_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.user_history_seq;
       public          mendrika    false            �           2613    30956    30956    BLOB     &   SELECT pg_catalog.lo_create('30956');
 &   SELECT pg_catalog.lo_unlink('30956');
                mendrika    false            �           2613    30957    30957    BLOB     &   SELECT pg_catalog.lo_create('30957');
 &   SELECT pg_catalog.lo_unlink('30957');
                mendrika    false            �           2613    30958    30958    BLOB     &   SELECT pg_catalog.lo_create('30958');
 &   SELECT pg_catalog.lo_unlink('30958');
                mendrika    false            �           2613    30959    30959    BLOB     &   SELECT pg_catalog.lo_create('30959');
 &   SELECT pg_catalog.lo_unlink('30959');
                mendrika    false            �           2613    30960    30960    BLOB     &   SELECT pg_catalog.lo_create('30960');
 &   SELECT pg_catalog.lo_unlink('30960');
                mendrika    false            �           2613    30961    30961    BLOB     &   SELECT pg_catalog.lo_create('30961');
 &   SELECT pg_catalog.lo_unlink('30961');
                mendrika    false            �           2613    30962    30962    BLOB     &   SELECT pg_catalog.lo_create('30962');
 &   SELECT pg_catalog.lo_unlink('30962');
                mendrika    false            �          0    30618    app 
   TABLE DATA           �   COPY public.app (id, authorized_role_prefix, created_at, description, logo_url, name, removed_at, updated_at, url, created_by_id, removed_by_id, updated_by_id) FROM stdin;
    public          mendrika    false    209   j�       �          0    30625    file 
   TABLE DATA           _   COPY public.file (id, created_at, name, path, size, type, created_by_id, files_id) FROM stdin;
    public          mendrika    false    210   ��       �          0    30632    file_history 
   TABLE DATA           �   COPY public.file_history (id, created_at, name, path, removed_at, type, created_by_id, file_id, mail_id, removed_by_id) FROM stdin;
    public          mendrika    false    211   ��       �          0    30639    location 
   TABLE DATA           �   COPY public.location (id, created_at, latitude, longitude, name, removed_at, updated_at, created_by_id, removed_by_id, updated_by_id) FROM stdin;
    public          mendrika    false    212   ��       �          0    30644    mail 
   TABLE DATA           �   COPY public.mail (id, confidentiality, created_at, description, note_for_messenger, object, priority, receiver, reference, sender, status, created_by_id, receiver_location_id, receiver_user_id, sender_location_id, sender_user_id) FROM stdin;
    public          mendrika    false    213   ��       �          0    30654 
   mail_files 
   TABLE DATA           7   COPY public.mail_files (mail_id, files_id) FROM stdin;
    public          mendrika    false    214   ��       �          0    30657    mail_history 
   TABLE DATA             COPY public.mail_history (id, created_at, description, mail_confidentiality, note_for_messenger, object, priority, receiver, reference, removed_at, sender, status, created_by_id, mail_id, receiver_location_id, removed_by_id, sender_location_id) FROM stdin;
    public          mendrika    false    215   i�       �          0    30667 	   mouvement 
   TABLE DATA           �   COPY public.mouvement (id, coordinates, description, end_date, estimated_delay, estimated_distance, receiver, sender, start_date, status, mail_id, messenger_id, receiver_location_id, receiver_user_id, sender_location_id, sender_user_id) FROM stdin;
    public          mendrika    false    216   ��       �          0    30675    notification 
   TABLE DATA           g   COPY public.notification (id, created_at, description, done_at, title, type, url, user_id) FROM stdin;
    public          mendrika    false    217   �       �          0    30683    role 
   TABLE DATA           �   COPY public.role (id, code, color, created_at, name, removed_at, updated_at, created_by_id, removed_by_id, updated_by_id) FROM stdin;
    public          mendrika    false    218   @�       �          0    30690    session 
   TABLE DATA           �   COPY public.session (id, created_at, expired_at, ip_address, last_activity_at, query_count, status, token_value, user_agent, user_id) FROM stdin;
    public          mendrika    false    219   ��       �          0    30698    sys_user 
   TABLE DATA           �   COPY public.sys_user (id, created_at, email, first_name, last_name, password, phone_number, status, created_by_id, location_id) FROM stdin;
    public          mendrika    false    220   (�       �          0    30706    sys_user_roles 
   TABLE DATA           ;   COPY public.sys_user_roles (user_id, roles_id) FROM stdin;
    public          mendrika    false    221   ��       �          0    30711    user_history 
   TABLE DATA           �   COPY public.user_history (id, created_at, email, first_name, last_name, password, phone_number, removed_at, status, created_by_id, removed_by_id, user_id) FROM stdin;
    public          mendrika    false    222   ��       �           0    0    file_history_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.file_history_seq', 1, false);
          public          mendrika    false    223            �           0    0    location_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.location_seq', 51, true);
          public          mendrika    false    224            �           0    0    mail_history_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.mail_history_seq', 1, false);
          public          mendrika    false    225            �           0    0    mouvement_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.mouvement_seq', 151, true);
          public          mendrika    false    226            �           0    0    role_seq    SEQUENCE SET     8   SELECT pg_catalog.setval('public.role_seq', 101, true);
          public          mendrika    false    227            �           0    0    user_history_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.user_history_seq', 1, false);
          public          mendrika    false    228            �          0    0    BLOBS    BLOBS                             false   ��       �           2606    30624    app app_pkey 
   CONSTRAINT     J   ALTER TABLE ONLY public.app
    ADD CONSTRAINT app_pkey PRIMARY KEY (id);
 6   ALTER TABLE ONLY public.app DROP CONSTRAINT app_pkey;
       public            mendrika    false    209            �           2606    30638    file_history file_history_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.file_history
    ADD CONSTRAINT file_history_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.file_history DROP CONSTRAINT file_history_pkey;
       public            mendrika    false    211            �           2606    30631    file file_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.file DROP CONSTRAINT file_pkey;
       public            mendrika    false    210            �           2606    30643    location location_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.location DROP CONSTRAINT location_pkey;
       public            mendrika    false    212                       2606    30666    mail_history mail_history_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT mail_history_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT mail_history_pkey;
       public            mendrika    false    215            �           2606    30653    mail mail_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT mail_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.mail DROP CONSTRAINT mail_pkey;
       public            mendrika    false    213                       2606    30674    mouvement mouvement_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT mouvement_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT mouvement_pkey;
       public            mendrika    false    216            	           2606    30682    notification notification_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT notification_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.notification DROP CONSTRAINT notification_pkey;
       public            mendrika    false    217                       2606    30689    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public            mendrika    false    218                       2606    30697    session session_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.session
    ADD CONSTRAINT session_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.session DROP CONSTRAINT session_pkey;
       public            mendrika    false    219                       2606    30705    sys_user sys_user_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT sys_user_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.sys_user DROP CONSTRAINT sys_user_pkey;
       public            mendrika    false    220                       2606    30710 "   sys_user_roles sys_user_roles_pkey 
   CONSTRAINT     o   ALTER TABLE ONLY public.sys_user_roles
    ADD CONSTRAINT sys_user_roles_pkey PRIMARY KEY (user_id, roles_id);
 L   ALTER TABLE ONLY public.sys_user_roles DROP CONSTRAINT sys_user_roles_pkey;
       public            mendrika    false    221    221                       2606    30730 &   mail_files uk32qiumave3gxnr6k18kmws6jw 
   CONSTRAINT     e   ALTER TABLE ONLY public.mail_files
    ADD CONSTRAINT uk32qiumave3gxnr6k18kmws6jw UNIQUE (files_id);
 P   ALTER TABLE ONLY public.mail_files DROP CONSTRAINT uk32qiumave3gxnr6k18kmws6jw;
       public            mendrika    false    214            �           2606    30720    app uk88vfgccvckwwip06k7tpf8uk3 
   CONSTRAINT     Z   ALTER TABLE ONLY public.app
    ADD CONSTRAINT uk88vfgccvckwwip06k7tpf8uk3 UNIQUE (name);
 I   ALTER TABLE ONLY public.app DROP CONSTRAINT uk88vfgccvckwwip06k7tpf8uk3;
       public            mendrika    false    209                       2606    30736 $   sys_user ukahtq5ew3v0kt1n7hf1sgp7p8l 
   CONSTRAINT     `   ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT ukahtq5ew3v0kt1n7hf1sgp7p8l UNIQUE (email);
 N   ALTER TABLE ONLY public.sys_user DROP CONSTRAINT ukahtq5ew3v0kt1n7hf1sgp7p8l;
       public            mendrika    false    220                       2606    30732     role ukc36say97xydpmgigg38qv5l2p 
   CONSTRAINT     [   ALTER TABLE ONLY public.role
    ADD CONSTRAINT ukc36say97xydpmgigg38qv5l2p UNIQUE (code);
 J   ALTER TABLE ONLY public.role DROP CONSTRAINT ukc36say97xydpmgigg38qv5l2p;
       public            mendrika    false    218                       2606    30734 #   session ukg53iu5ijtss8d2msunx6gflv2 
   CONSTRAINT     e   ALTER TABLE ONLY public.session
    ADD CONSTRAINT ukg53iu5ijtss8d2msunx6gflv2 UNIQUE (token_value);
 M   ALTER TABLE ONLY public.session DROP CONSTRAINT ukg53iu5ijtss8d2msunx6gflv2;
       public            mendrika    false    219            �           2606    30726 $   location uklyuhh55j16k5bte40xi0pmd4c 
   CONSTRAINT     n   ALTER TABLE ONLY public.location
    ADD CONSTRAINT uklyuhh55j16k5bte40xi0pmd4c UNIQUE (latitude, longitude);
 N   ALTER TABLE ONLY public.location DROP CONSTRAINT uklyuhh55j16k5bte40xi0pmd4c;
       public            mendrika    false    212    212                       2606    30728     mail ukp825ewgdq7rjb1qqexulwcug6 
   CONSTRAINT     `   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT ukp825ewgdq7rjb1qqexulwcug6 UNIQUE (reference);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT ukp825ewgdq7rjb1qqexulwcug6;
       public            mendrika    false    213            �           2606    30722    app ukrg6bwse2t4o9p872x6pqswap5 
   CONSTRAINT     Y   ALTER TABLE ONLY public.app
    ADD CONSTRAINT ukrg6bwse2t4o9p872x6pqswap5 UNIQUE (url);
 I   ALTER TABLE ONLY public.app DROP CONSTRAINT ukrg6bwse2t4o9p872x6pqswap5;
       public            mendrika    false    209            �           2606    30724     file ukrmk4dly78ko7osvff9wt4obkx 
   CONSTRAINT     m   ALTER TABLE ONLY public.file
    ADD CONSTRAINT ukrmk4dly78ko7osvff9wt4obkx UNIQUE (path, name, size, type);
 J   ALTER TABLE ONLY public.file DROP CONSTRAINT ukrmk4dly78ko7osvff9wt4obkx;
       public            mendrika    false    210    210    210    210                       2606    30718    user_history user_history_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT user_history_pkey PRIMARY KEY (id);
 H   ALTER TABLE ONLY public.user_history DROP CONSTRAINT user_history_pkey;
       public            mendrika    false    222            /           2606    30848 (   mail_history fk1viw7yj2c66qt1upbyowidkxq    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT fk1viw7yj2c66qt1upbyowidkxq FOREIGN KEY (receiver_location_id) REFERENCES public.location(id);
 R   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT fk1viw7yj2c66qt1upbyowidkxq;
       public          mendrika    false    212    215    3579            8           2606    30893 (   notification fk32m607iu5gp2w3irdpj1pg3nm    FK CONSTRAINT     �   ALTER TABLE ONLY public.notification
    ADD CONSTRAINT fk32m607iu5gp2w3irdpj1pg3nm FOREIGN KEY (user_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.notification DROP CONSTRAINT fk32m607iu5gp2w3irdpj1pg3nm;
       public          mendrika    false    3603    217    220                       2606    30753    app fk3co08i892h476qldkewpacn0d    FK CONSTRAINT     �   ALTER TABLE ONLY public.app
    ADD CONSTRAINT fk3co08i892h476qldkewpacn0d FOREIGN KEY (updated_by_id) REFERENCES public.sys_user(id);
 I   ALTER TABLE ONLY public.app DROP CONSTRAINT fk3co08i892h476qldkewpacn0d;
       public          mendrika    false    3603    209    220            %           2606    30788 #   location fk469fpmde3nals3lc7yinb4e2    FK CONSTRAINT     �   ALTER TABLE ONLY public.location
    ADD CONSTRAINT fk469fpmde3nals3lc7yinb4e2 FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 M   ALTER TABLE ONLY public.location DROP CONSTRAINT fk469fpmde3nals3lc7yinb4e2;
       public          mendrika    false    212    3603    220            3           2606    30868 %   mouvement fk4y7e2o03qqppkdmxri34alotd    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fk4y7e2o03qqppkdmxri34alotd FOREIGN KEY (messenger_id) REFERENCES public.sys_user(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fk4y7e2o03qqppkdmxri34alotd;
       public          mendrika    false    216    3603    220            .           2606    30843 (   mail_history fk6ngq0arc8yvltjsmq5uwg1xlp    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT fk6ngq0arc8yvltjsmq5uwg1xlp FOREIGN KEY (mail_id) REFERENCES public.mail(id) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT fk6ngq0arc8yvltjsmq5uwg1xlp;
       public          mendrika    false    213    3583    215                        2606    30773 (   file_history fk78n6m4dtl089ohmk1kwqnh22q    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_history
    ADD CONSTRAINT fk78n6m4dtl089ohmk1kwqnh22q FOREIGN KEY (file_id) REFERENCES public.file(id);
 R   ALTER TABLE ONLY public.file_history DROP CONSTRAINT fk78n6m4dtl089ohmk1kwqnh22q;
       public          mendrika    false    211    3573    210            #           2606    30793 $   location fk7j5d8bu55wy1tw215x7509iq6    FK CONSTRAINT     �   ALTER TABLE ONLY public.location
    ADD CONSTRAINT fk7j5d8bu55wy1tw215x7509iq6 FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 N   ALTER TABLE ONLY public.location DROP CONSTRAINT fk7j5d8bu55wy1tw215x7509iq6;
       public          mendrika    false    212    3603    220            "           2606    30783 (   file_history fk8woxb5thp2wws6xc5nnqs0cex    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_history
    ADD CONSTRAINT fk8woxb5thp2wws6xc5nnqs0cex FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.file_history DROP CONSTRAINT fk8woxb5thp2wws6xc5nnqs0cex;
       public          mendrika    false    220    3603    211            A           2606    30938 (   user_history fk91g3ndmxslx8qpafnqu1wbs4b    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT fk91g3ndmxslx8qpafnqu1wbs4b FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.user_history DROP CONSTRAINT fk91g3ndmxslx8qpafnqu1wbs4b;
       public          mendrika    false    222    220    3603            ?           2606    30928 *   sys_user_roles fk91m1qswm862aeo6a3ps1hfc32    FK CONSTRAINT     �   ALTER TABLE ONLY public.sys_user_roles
    ADD CONSTRAINT fk91m1qswm862aeo6a3ps1hfc32 FOREIGN KEY (roles_id) REFERENCES public.role(id);
 T   ALTER TABLE ONLY public.sys_user_roles DROP CONSTRAINT fk91m1qswm862aeo6a3ps1hfc32;
       public          mendrika    false    3595    221    218            9           2606    30898     role fk9ed2sev92saw27laifsugsbhw    FK CONSTRAINT     �   ALTER TABLE ONLY public.role
    ADD CONSTRAINT fk9ed2sev92saw27laifsugsbhw FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.role DROP CONSTRAINT fk9ed2sev92saw27laifsugsbhw;
       public          mendrika    false    3603    220    218            >           2606    30923 $   sys_user fk9m0tyfhu843hnt2bvvh79pp8i    FK CONSTRAINT     �   ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT fk9m0tyfhu843hnt2bvvh79pp8i FOREIGN KEY (location_id) REFERENCES public.location(id);
 N   ALTER TABLE ONLY public.sys_user DROP CONSTRAINT fk9m0tyfhu843hnt2bvvh79pp8i;
       public          mendrika    false    212    220    3579            !           2606    30778 (   file_history fk9y4marxu4ew1fn9aw1j5b89fr    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_history
    ADD CONSTRAINT fk9y4marxu4ew1fn9aw1j5b89fr FOREIGN KEY (mail_id) REFERENCES public.mail(id);
 R   ALTER TABLE ONLY public.file_history DROP CONSTRAINT fk9y4marxu4ew1fn9aw1j5b89fr;
       public          mendrika    false    211    3583    213            C           2606    30948 (   user_history fka6c748rpw3wl42s1359usrbqn    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT fka6c748rpw3wl42s1359usrbqn FOREIGN KEY (user_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.user_history DROP CONSTRAINT fka6c748rpw3wl42s1359usrbqn;
       public          mendrika    false    220    3603    222            ;           2606    30908     role fkardx7vu9b7750rfxnshl1xj3x    FK CONSTRAINT     �   ALTER TABLE ONLY public.role
    ADD CONSTRAINT fkardx7vu9b7750rfxnshl1xj3x FOREIGN KEY (updated_by_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.role DROP CONSTRAINT fkardx7vu9b7750rfxnshl1xj3x;
       public          mendrika    false    3603    220    218            '           2606    30808     mail fkb1wkn0sfxqxokj66ohl6pjqjr    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT fkb1wkn0sfxqxokj66ohl6pjqjr FOREIGN KEY (receiver_location_id) REFERENCES public.location(id);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT fkb1wkn0sfxqxokj66ohl6pjqjr;
       public          mendrika    false    213    3579    212            2           2606    30863 %   mouvement fke5yuhh3dabo3wymaqu490jjxw    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fke5yuhh3dabo3wymaqu490jjxw FOREIGN KEY (mail_id) REFERENCES public.mail(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fke5yuhh3dabo3wymaqu490jjxw;
       public          mendrika    false    216    3583    213            B           2606    30943 (   user_history fkfosngy98vsk83ddqhn0dyefce    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_history
    ADD CONSTRAINT fkfosngy98vsk83ddqhn0dyefce FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.user_history DROP CONSTRAINT fkfosngy98vsk83ddqhn0dyefce;
       public          mendrika    false    3603    222    220                       2606    30758     file fkfpp9sok8mgeo0rhtusmgee2gg    FK CONSTRAINT     �   ALTER TABLE ONLY public.file
    ADD CONSTRAINT fkfpp9sok8mgeo0rhtusmgee2gg FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.file DROP CONSTRAINT fkfpp9sok8mgeo0rhtusmgee2gg;
       public          mendrika    false    3603    220    210            -           2606    30838 (   mail_history fkg91etjj2bylwk7w97yty6517l    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT fkg91etjj2bylwk7w97yty6517l FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT fkg91etjj2bylwk7w97yty6517l;
       public          mendrika    false    220    3603    215            1           2606    30858 (   mail_history fkhviolly76b9c0jlpi54soj9bn    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT fkhviolly76b9c0jlpi54soj9bn FOREIGN KEY (sender_location_id) REFERENCES public.location(id);
 R   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT fkhviolly76b9c0jlpi54soj9bn;
       public          mendrika    false    215    3579    212            +           2606    30828 &   mail_files fki7ge89v3xhr8vehrtjdbeq4ts    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_files
    ADD CONSTRAINT fki7ge89v3xhr8vehrtjdbeq4ts FOREIGN KEY (files_id) REFERENCES public.file(id);
 P   ALTER TABLE ONLY public.mail_files DROP CONSTRAINT fki7ge89v3xhr8vehrtjdbeq4ts;
       public          mendrika    false    210    3573    214            5           2606    30878 %   mouvement fkidwmj08qfwhn7iwa8i0g8b2sc    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fkidwmj08qfwhn7iwa8i0g8b2sc FOREIGN KEY (receiver_user_id) REFERENCES public.sys_user(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fkidwmj08qfwhn7iwa8i0g8b2sc;
       public          mendrika    false    220    216    3603            &           2606    30803     mail fkinkmiogynv3fk7jn0j4cgthcm    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT fkinkmiogynv3fk7jn0j4cgthcm FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT fkinkmiogynv3fk7jn0j4cgthcm;
       public          mendrika    false    3603    213    220                       2606    30748    app fkjngua8fc1wg3ygjfr2inqg2nw    FK CONSTRAINT     �   ALTER TABLE ONLY public.app
    ADD CONSTRAINT fkjngua8fc1wg3ygjfr2inqg2nw FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 I   ALTER TABLE ONLY public.app DROP CONSTRAINT fkjngua8fc1wg3ygjfr2inqg2nw;
       public          mendrika    false    209    220    3603            (           2606    30813     mail fkk636jqve1k0hjov5h3r9tbf7o    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT fkk636jqve1k0hjov5h3r9tbf7o FOREIGN KEY (receiver_user_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT fkk636jqve1k0hjov5h3r9tbf7o;
       public          mendrika    false    213    3603    220            =           2606    30918 $   sys_user fkkg6bp76acdeeklv8yxr2cxlft    FK CONSTRAINT     �   ALTER TABLE ONLY public.sys_user
    ADD CONSTRAINT fkkg6bp76acdeeklv8yxr2cxlft FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 N   ALTER TABLE ONLY public.sys_user DROP CONSTRAINT fkkg6bp76acdeeklv8yxr2cxlft;
       public          mendrika    false    3603    220    220            4           2606    30873 %   mouvement fkkvssdi7xtchj04x624lcl9f7p    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fkkvssdi7xtchj04x624lcl9f7p FOREIGN KEY (receiver_location_id) REFERENCES public.location(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fkkvssdi7xtchj04x624lcl9f7p;
       public          mendrika    false    216    3579    212            6           2606    30883 %   mouvement fkld826imfi17rh4wxuku03qsfs    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fkld826imfi17rh4wxuku03qsfs FOREIGN KEY (sender_location_id) REFERENCES public.location(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fkld826imfi17rh4wxuku03qsfs;
       public          mendrika    false    212    216    3579            :           2606    30903     role fkm1jy0tf1qhbjad1erb0tm0brg    FK CONSTRAINT     �   ALTER TABLE ONLY public.role
    ADD CONSTRAINT fkm1jy0tf1qhbjad1erb0tm0brg FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.role DROP CONSTRAINT fkm1jy0tf1qhbjad1erb0tm0brg;
       public          mendrika    false    220    3603    218            )           2606    30818     mail fkmqrmmmdo2b6bmk4ergpgkodf4    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT fkmqrmmmdo2b6bmk4ergpgkodf4 FOREIGN KEY (sender_location_id) REFERENCES public.location(id);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT fkmqrmmmdo2b6bmk4ergpgkodf4;
       public          mendrika    false    213    3579    212            $           2606    30798 $   location fkmqxeeegeaqi7pdv4pbu5jm0td    FK CONSTRAINT     �   ALTER TABLE ONLY public.location
    ADD CONSTRAINT fkmqxeeegeaqi7pdv4pbu5jm0td FOREIGN KEY (updated_by_id) REFERENCES public.sys_user(id);
 N   ALTER TABLE ONLY public.location DROP CONSTRAINT fkmqxeeegeaqi7pdv4pbu5jm0td;
       public          mendrika    false    212    3603    220                       2606    30743    app fkn4p6b52y0bp6t4fu5uvbrqtpj    FK CONSTRAINT     �   ALTER TABLE ONLY public.app
    ADD CONSTRAINT fkn4p6b52y0bp6t4fu5uvbrqtpj FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 I   ALTER TABLE ONLY public.app DROP CONSTRAINT fkn4p6b52y0bp6t4fu5uvbrqtpj;
       public          mendrika    false    220    209    3603            <           2606    30913 #   session fknw9tf6wh53wimqoytxuqqqi3n    FK CONSTRAINT     �   ALTER TABLE ONLY public.session
    ADD CONSTRAINT fknw9tf6wh53wimqoytxuqqqi3n FOREIGN KEY (user_id) REFERENCES public.sys_user(id);
 M   ALTER TABLE ONLY public.session DROP CONSTRAINT fknw9tf6wh53wimqoytxuqqqi3n;
       public          mendrika    false    220    3603    219            @           2606    30933 *   sys_user_roles fkp2804vh0ea810pitigxq5n6pn    FK CONSTRAINT     �   ALTER TABLE ONLY public.sys_user_roles
    ADD CONSTRAINT fkp2804vh0ea810pitigxq5n6pn FOREIGN KEY (user_id) REFERENCES public.sys_user(id);
 T   ALTER TABLE ONLY public.sys_user_roles DROP CONSTRAINT fkp2804vh0ea810pitigxq5n6pn;
       public          mendrika    false    221    3603    220            ,           2606    30833 &   mail_files fkppolora6bxso22igoh9kyophb    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_files
    ADD CONSTRAINT fkppolora6bxso22igoh9kyophb FOREIGN KEY (mail_id) REFERENCES public.mail(id);
 P   ALTER TABLE ONLY public.mail_files DROP CONSTRAINT fkppolora6bxso22igoh9kyophb;
       public          mendrika    false    213    3583    214                       2606    30768 (   file_history fks5hueessi5eqglvmbgq23fusk    FK CONSTRAINT     �   ALTER TABLE ONLY public.file_history
    ADD CONSTRAINT fks5hueessi5eqglvmbgq23fusk FOREIGN KEY (created_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.file_history DROP CONSTRAINT fks5hueessi5eqglvmbgq23fusk;
       public          mendrika    false    220    3603    211                       2606    30763     file fksx5vcxfpoofejq2vtsou1hj1l    FK CONSTRAINT     �   ALTER TABLE ONLY public.file
    ADD CONSTRAINT fksx5vcxfpoofejq2vtsou1hj1l FOREIGN KEY (files_id) REFERENCES public.mail_history(id);
 J   ALTER TABLE ONLY public.file DROP CONSTRAINT fksx5vcxfpoofejq2vtsou1hj1l;
       public          mendrika    false    210    3589    215            0           2606    30853 (   mail_history fkt62tsmac8k8kvyf9k253q04l3    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail_history
    ADD CONSTRAINT fkt62tsmac8k8kvyf9k253q04l3 FOREIGN KEY (removed_by_id) REFERENCES public.sys_user(id);
 R   ALTER TABLE ONLY public.mail_history DROP CONSTRAINT fkt62tsmac8k8kvyf9k253q04l3;
       public          mendrika    false    215    3603    220            *           2606    30823     mail fktar48t7jbgmxy1dfd4srgauys    FK CONSTRAINT     �   ALTER TABLE ONLY public.mail
    ADD CONSTRAINT fktar48t7jbgmxy1dfd4srgauys FOREIGN KEY (sender_user_id) REFERENCES public.sys_user(id);
 J   ALTER TABLE ONLY public.mail DROP CONSTRAINT fktar48t7jbgmxy1dfd4srgauys;
       public          mendrika    false    213    3603    220            7           2606    30888 %   mouvement fktebaio3bidfxuiradyhjhsdf1    FK CONSTRAINT     �   ALTER TABLE ONLY public.mouvement
    ADD CONSTRAINT fktebaio3bidfxuiradyhjhsdf1 FOREIGN KEY (sender_user_id) REFERENCES public.sys_user(id);
 O   ALTER TABLE ONLY public.mouvement DROP CONSTRAINT fktebaio3bidfxuiradyhjhsdf1;
       public          mendrika    false    3603    220    216            �   $  x��нj�0�Y~
��l]�D?[�Pm�R:�,+�!�������ȋ�]J		t�v�sρ�ю�"��D��#�k��+�۠�,���u�z�/�o�Q&P� Sf���j��1ͽ�|t!aw|�)�xL>d�-�`�f��mc.�SJ�s(��}3�8�>�s}{�r∻��0��X�0����j��fFs�n� 	0�n���� '�V^�o68��/��OeO���/$�X�,5�3e -�S<OF�����3��UB����}T��������0�ܳi�q�h]WU����      �   �   x���;r�0Dk�� d���!r7�z�qb�H��i�d�ݷ���`T�hVAm���5IV�֜G�<�!]I��s)�9���c��0o����xj�/��5�&�BD� �o��Ǝ�����t����������;�1IN���# b
Bjɺ��O��}h7��X�:� ��B�\��S�����������כ�����ƠQ	"	%�J��,"���z�����Ʒy��o}kb1      �      x������ � �      �   �  x���M��0���)�cC\�R���!8��L9v��9�/F;�𰘙�*WʱU�ׯ_?Qf��ao� �܋��c.ޒ�`]F!gkc �9����X��<�������}��U;��pw4�S[���������bD����O\�0�>���%���ޡs$ץ;���bA� �{/1�G��
D�+�������7s�L�!Mà��@�(��\D�g�?g��nv")(�A7y,�E����Z�8�Ȱ�_����l̙I��ma����Ӈ��
�<�����3g�?�s�{�X���^� .>
/� }�_	Ö�
�9�βZ#��=PprT8��\��vӨ ��U����8��Mcݵ�Y��IfYT�OS�rʸ��]ѣ��Φ��c��	���(;֭n>��+��ا�i�bN�]S����X����U��F��@�x��H��h:]�O.ӼI���j�/~%�S,R��tV,&�b��:���%�
�N]?�oU�q>w}�t�b������������98�������"jTa�씚T��IMVV���f%�ed�H~%�梊���9���i�EU��p󻂓9���UӝN�ֶ���/�����E���?^���zL���`N�0V7���ew<Nm]^����m+���\�c��[���k���t�Ӂ���l�V�_N�>��v?�$       �   _  x���ˎ�0���Sd7+�cwWaTi((S��l���II���4,�x���܀��� �"�b�����=+X�4�WTit����Hk ���|v>]dD0�(gT�������	�����qS�U���j�$`�����j}}c��,���v�ոº'o�l� ��I�c�d���w�q���A �X�d�˦�ڪ�<��XX�q�qۮ�
�����q���j,	g���^	�pT)���uH��ԧV!jnFβ�<[܇!'̌�4F�9��M�'����V��Kb�n�K2�����d����&�����o���l~rXxE�׈��&��)���	V	ΐ��P:%A�gS�T�Q�`(H��#��r�M�sk�#�9s�S���E�W�6�%E;�n��hG��갻e��3�0�؛r�ꦻ|��?fKv:;�򃩥�`�E��"E���=M��Dx%<�R��&R���Q�efu��X6ьHeS��_��O�g�\[����m��;g����JAc���� z}`pbZm�,4�R��* Ema��y	�y���&�1g֦淊��⪱T���bdͲ
�џ�o����C&v���\�G��Oj���      �   b   x��̱�0����<X�H��	��K�Kp��[[5�v#l�"G:��t�޿�$Op̈́��5��󎞾Y��L���~�:��vL'ԨF�)�CD �'/      �      x������ � �      �   �  x��R�j1<k�b��V�g/�6�CN��F-g��������X4Y�a:�Q]�*�#e0y���.�N�c��7����[�Y�e�yy<+^O<���qgV�dA#�ޡ�[�#��x���9|��|�tw�U����G��Ʀ�	D��b2s�k��*gA0RH>��N��,�z,5����lD&i'��xH[��F�����`#e���Ai
���g~Z&i&�e�n�r3C�x��rn��L�����|�����8�3���Xfq�	rLٰ�,�iW0�.���� %* I�RXPr��+ax�s�5Ы�����d��	��/?fٝ�?/��_:����Q��CȜ�	�}�eK�}�!�W7�c�����~ā�.      �     x��ѽN�@�9y���S�W6$*�
!����I���xҵlx�����sJ�9�Q2hU�h@U�*�3w��@L�$q7�(Icw]�m��:�^�e��l��ib?&��{�m���r��{]�����-p <୨{|8=_�ç-ۡ!ϬU���%!X4&��g,���V[�A]j�b�X�
�>'������H�E��g�T,d���c�X��;^yB#�p���pr2��2�3UB3�e�k`��X|���W'M�W� �������>�}�t�*      �   0  x����J�0��ӧ�4����nh����V��T.i��H>�/�jqz1Q8�?���;��ղ���it���oM|F�P�I),(B��"�����v7��]D�j `���p)��\Z�Z�m�e�e���	���������I��,g�Df����C|�N����s��8��\�f�Z��붼�ʦE�h���c�8�,�B(��X��.L��5e��J7��.z21�p��N$�
h����ءT�%��d�[���nu�.�Zϯ�SOTQ�c����|xta0>�߷��I��9�D      �   �  x�͖[o�6���_��]�T83�*�Cj;����v.,`�"�(q|�e7ί/��kMѾ����Q��sΨ"��dZ)˄ȼ�$C=�T!C��g9��d)�Pą�?� �t)d���3^��`e�Ҋ����b�=?����ݙJ�}2�V���`vC�;�R�;�kd���$�lC��l��L&nC<���f�0�����Ӈ8�Ӎ�p����� ��c�5�O�Y�{�Æ$]��?��F��~�'�M�wbu3�����f�q���ʇ�vw͏G2▼s5#�k&d��>rFN[m�U�� ^Z�t-�	��9W9氮�j	F�5�ܔ$
�$��	W*!�o���V��!b���O����d�7�������"��]:Zt��k�=�gOj��Ӎ]�X�b]+2��M`#y��s.ɂ�2֭��UH`k-�!W1���kH�)�-�JkW��cVH#��/���g�_��>��C�m;���x���ۓG�t1�]����y�ϯf�Ύ1��⊀Qn��+d�b`т�E�lI�F&�2�@3`ӕL���
�WY�,�c�`�-����`�܅$�����hp2�d_Vy}j��O���4�͚�m�G�S���A��j�I�/��R�J��02��3�Q"U{�n�C��):�ߖ���\�E$^���h�%����KTV��c�-/��,�s�TR�+_�K�K��J�U�$�}~(IF�7y��V�2@m^�ˮ�bP9�k�Ǌ��x�c��}�t�;׃���71���\��
�N�CV�L�R��4�+0U�#@$l��hb�SR�I�uUQ�j=̨Lo4d-��(J0�1�۷��K�����u�B�z�Y�T�������0�Ϗ�U�vv�G���^�VHA!��NY�lҢ՘�s�[t��R�Z��^�V      �   g  x���ɒ�8���)��W���4���Vs�l��T�M?G��*��/L����;?��z��x�}@!a@�P �s��!���S� ��DO�$�eX F���d���I�,Z)70G��h�,J����J�����w����Ѥ�8�JP[��ru8�Mu:銍m��i/��1)��t_J^��@B��@N��)�;5�!�Q`sU�G�"ŀ'||��aH!T:<'53!4��<<�C@=�4�7R��'�\L�JȾ�l���8_��]�":3��FӰ�f՚է��V-k��
��}�5˰:��ȱ�	���ieP&-�(�)���H�3�=|"���1|�݃�U8G߉v��]�*�[��Y�e7��}w<N���.�3S�	������7ѠS(���P�,G�$Z��0fWZ�RH���;4t=�I�����L2��o��N]��*K��7��-�/�A��ƫ���u���*=��}��ׅ�
��H4f��:_���<��P�(�6,�@Ԁ
)q �D2��F�;<*���B(BNf|��GV���^�D����ƻܴ�8Z���_�/����U�j��IelbV�<]��	'͉|k��bH6o�4�O>I��!�#D2!B<�m�q�� aH}̌F�k�⋽`�B.�����,��~�\GNm�j�B�n�A3�6�r2>�7�j�s�|<̄�d;V'{?�l[k��W�M
�=�qˈ�`�����P�S��c�a�SI�(��6�X��>�OD�q��|�U���S�$��X-7�J~;������}��nяb발��k�l�7E�ۅ�n��KfϽ`2��UH>�I�<�5�Vc� �aȆ�B+��1��d�\]+��ĕ�ߩgTl^峆���.���^s6\���,��}�w+�ekg�W\ZSK���E�X��U��)c���a0\��R~L@�c(�JrBm���ʷ��$��p������sB@�B��C/��~�mQf��I/*����vir����<t��;��ηIU)wT�J̴��n��g~����o�b��%u��A��z�H/�����7|��]�	����u�|e�ދt�^/S1��ۅ�#��.����r���w�      �   �   x���ˍ  ��^���^�
�����bL�LNJ�ɸ0Q\jüKYK?<ԉ�-����n�6��g�Ac�/Fo�-��\m�j��K��|�1�$r��Ϥ��D�3�0�yc�Y���T�a��mM@D#O�j���e@��m�m��UbvX��&h�B�6�_D"�o۶�	ݏ&N}x�^+�f� ��=oc�gӽ+���V�g70�˚AVmS�E���z�7u���¥s��D�}�����c��$l7      �      x������ � �      �   �x   P  x�uTˎ�0��?[ �	�
4�/����L{YȒV�������=�i�����7F�I%���3�:P���qg9m
�E9�l*��Y��31�1Bc��=�ǠnxI��q9�-��9΅�b$Ae#d��������]O����r�T�`d����� ������(����$s��a(�8~�r<�ߚa��6ze�̻��/�~���������tÄ))AxTV�{D����5��%�-r�g)��&d4���FIJK�XØ�5N���d��2�^	j&��ؓV���FDE�2�=B�S�Df{�HSF��k���6���q���}�N��T��Mx�B����c3�7I* ?85��U��6���8�=��L�h1e�wP3\��s�{2U,K�A̛p+�F�n�j%�X�J��WS�*%=E�"�*+���h+�U�����{�#H5V��j�i�#k<�g�}�����)8;���7#�BB����qk����1k	ȉ����+��b�D��
IR�/��MV#�����!��Ea�)ۍ�X���@��*�9�ᩴ]!�f[Sx��L�nc��u��      �x   P  x�uTˎ�0��?[ �	�
4�/����L{YȒV�������=�i�����7F�I%���3�:P���qg9m
�E9�l*��Y��31�1Bc��=�ǠnxI��q9�-��9΅�b$Ae#d��������]O����r�T�`d����� ������(����$s��a(�8~�r<�ߚa��6ze�̻��/�~���������tÄ))AxTV�{D����5��%�-r�g)��&d4���FIJK�XØ�5N���d��2�^	j&��ؓV���FDE�2�=B�S�Df{�HSF��k���6���q���}�N��T��Mx�B����c3�7I* ?85��U��6���8�=��L�h1e�wP3\��s�{2U,K�A̛p+�F�n�j%�X�J��WS�*%=E�"�*+���h+�U�����{�#H5V��j�i�#k<�g�}�����)8;���7#�BB����qk����1k	ȉ����+��b�D��
IR�/��MV#�����!��Ea�)ۍ�X���@��*�9�ᩴ]!�f[Sx��L�nc��u��      �x   P  x�uTˎ�0��?[ �	�
4�/����L{YȒV�������=�i�����7F�I%���3�:P���qg9m
�E9�l*��Y��31�1Bc��=�ǠnxI��q9�-��9΅�b$Ae#d��������]O����r�T�`d����� ������(����$s��a(�8~�r<�ߚa��6ze�̻��/�~���������tÄ))AxTV�{D����5��%�-r�g)��&d4���FIJK�XØ�5N���d��2�^	j&��ؓV���FDE�2�=B�S�Df{�HSF��k���6���q���}�N��T��Mx�B����c3�7I* ?85��U��6���8�=��L�h1e�wP3\��s�{2U,K�A̛p+�F�n�j%�X�J��WS�*%=E�"�*+���h+�U�����{�#H5V��j�i�#k<�g�}�����)8;���7#�BB����qk����1k	ȉ����+��b�D��
IR�/��MV#�����!��Ea�)ۍ�X���@��*�9�ᩴ]!�f[Sx��L�nc��u��      �x   �  x�E��m0Cj+�u����b���	6���I�9����3*����M��P#��?�*?�e��W�ټ�e"E&+��Xr ���֑<虀���7l��a(d��P&�֐Ήk �/��KGȫd3݄���j\�'�7]:;|󢺷�B�O�ا����8����m�u���:��1�����SVR��:����s9��#�8N�8��`������"�ޮ'_�Ã�=E�}"ߣ�σG?{���P��B��sȋ���x?�B�RlG��9�Z`�o!��;;<Me���9l��39����Ν�&K
��������ȱN9�2A�8���0�O�C������k�}o���Li�������;�R�������BS�      �x   P  x�uTˎ�0��?[ �	�
4�/����L{YȒV�������=�i�����7F�I%���3�:P���qg9m
�E9�l*��Y��31�1Bc��=�ǠnxI��q9�-��9΅�b$Ae#d��������]O����r�T�`d����� ������(����$s��a(�8~�r<�ߚa��6ze�̻��/�~���������tÄ))AxTV�{D����5��%�-r�g)��&d4���FIJK�XØ�5N���d��2�^	j&��ؓV���FDE�2�=B�S�Df{�HSF��k���6���q���}�N��T��Mx�B����c3�7I* ?85��U��6���8�=��L�h1e�wP3\��s�{2U,K�A̛p+�F�n�j%�X�J��WS�*%=E�"�*+���h+�U�����{�#H5V��j�i�#k<�g�}�����)8;���7#�BB����qk����1k	ȉ����+��b�D��
IR�/��MV#�����!��Ea�)ۍ�X���@��*�9�ᩴ]!�f[Sx��L�nc��u��      �x   �  x�E��m0Cj+�u����b���	6���I�9����3*����M��P#��?�*?�e��W�ټ�e"E&+��Xr ���֑<虀���7l��a(d��P&�֐Ήk �/��KGȫd3݄���j\�'�7]:;|󢺷�B�O�ا����8����m�u���:��1�����SVR��:����s9��#�8N�8��`������"�ޮ'_�Ã�=E�}"ߣ�σG?{���P��B��sȋ���x?�B�RlG��9�Z`�o!��;;<Me���9l��39����Ν�&K
��������ȱN9�2A�8���0�O�C������k�}o���Li�������;�R�������BS�      �x   �   x�E�Yn�@�������j4"��i4�'�������Zm�}�?V�am��8�Q8���Nj>��J.�Y)�����.�7p؞�3��*Wp#�渕Z`sw���`��n�Fl���N �4�p�M��v���<	6Ѯ��{8S�6D��)M\�X4�����a�7 �;��W��:��4YT>��(T���?{�1\�[7ڵ��c!M��i@��i��(����8�a��2��*�,i�1|Y�1�?Qn��          