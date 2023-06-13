SELECT id_sponsor, created_date, email, external_id, name, password
	FROM public.sponsor;
	
SELECT id_child, external_id, user_creator, name, nickname, password, age, created_date
	FROM public.child;
	
SELECT id_sponsor, id_child FROM public.sponsor_child;

SELECT id_task, external_id, name, description, weight, id_sponsor, created_date
	FROM public.task;
	
SELECT id_total, created_date, description, external_id, total, id_child, id_sponsor
	FROM public."total_monthly_amount";
	
SELECT id_total FROM total_monthly_amount WHERE id_sponsor = 1;

/*
DROP TABLE total-monthly-amount
DROP TABLE public.task
DROP TABLE public.task
DROP TABLE public.sponsor_child
DROP TABLE public.child
DROP TABLE public.sponsor
*/