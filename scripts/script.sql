SELECT id_sponsor, created_date, email, external_id, name, password
	FROM public.sponsor;
	
SELECT id_child, external_id, user_creator, name, nickname, password, age, created_date
	FROM public.child;
	
SELECT id_sponsor, id_child FROM public.sponsor_child;

SELECT id, created_date, email, external_id, name, password
	FROM public.tb_user;
	
SELECT id_total, created_date, description, external_id, total, id_child, id_sponsor
	FROM public."total_monthly_amount";
	
SELECT id_bonus, bonus, created_date, external_id, id_sponsor
	FROM public.bonus;
	
SELECT id_task, external_id, name, description, weight, id_sponsor, created_date
FROM public.task;

SELECT id_penalties, created_date, external_id, penalty, id_sponsor
	FROM public.penalty;

/*
DROP TABLE public.penalty
DROP TABLE public.bonus
DROP TABLE public.total-monthly-amount
DROP TABLE public.task
DROP TABLE public.task
DROP TABLE public.sponsor_child
DROP TABLE public.child
DROP TABLE public.sponsor
*/