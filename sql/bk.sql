select created_at, email, first_name, last_name, phone_number, status from system_user;

-- mail
select
    m.created_at,
    m.reference,
    m.confidentiality,
    m.priority,
    m.sender,
    ls.name,
    ls.latitude,
    ls.longitude,
    m.receiver,
    lr.name,
    lr.latitude,
    lr.longitude,
    mv.end_date
from mail m
         left join location ls on m.sender_location_id = ls.id
         left join location lr on m.receiver_location_id = lr.id
        left join mouvement mv on m.id = mv.mail_id order by mv.start_date desc;

-- mouvement
select
    m.start_date,
    m.end_date,
    m.end_date - m.start_date,
    m.sender,
    ls.name,
    ls.latitude,
    ls.longitude,
    m.receiver,
    lr.name,
    lr.latitude,
    lr.longitude,
    u.first_name || ' ' || u.last_name as messenger
from mouvement m
         left join location ls on m.sender_location_id = ls.id
         left join location lr on m.receiver_location_id = lr.id
         left join system_user u on messenger_id = u.id
group by m.start_date,
         m.end_date,
         m.sender,
         ls.name,
         ls.latitude,
         ls.longitude,
         m.receiver,
         lr.name,
         lr.latitude,
         lr.longitude,
         m.status,
         messenger;



--liste table avec avg ... --- list column                           type
--- row (mesures) count, sum, avg        count
-- filtre
-- order by
-- limit


-- Date: date time, date, time, jours, mois, année, jour de la semaine, heure, minute, seconde


select
    ls.name,
    avg(m.end_date - m.start_date)
from mouvement m
         left join location ls on m.sender_location_id = ls.id
         left join location lr on m.receiver_location_id = lr.id
         left join system_user u on messenger_id = u.id
group by
    ls.name;