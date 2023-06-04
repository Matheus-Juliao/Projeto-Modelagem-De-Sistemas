SELECT id_sponsor, created_date, email, external_id, name, password
	FROM public.sponsor;
	
SELECT id_child, external_id,  external_id_sponsor, name, nickname, password, age, created_date
	FROM public.child;