SELECT 
	tm1.name AS from_n,
	tm1.id AS from_id,
	cm1.semester AS from_sem,
	tm2.name AS to_n,
	tm2.id AS to_id,
	cm2.semester AS to_sem,
	cr.value AS count
FROM
	courserelationmap AS cr,
	coursemap AS cm1,
	topicmap AS tm1,
	coursemap AS cm2,
	topicmap AS tm2
WHERE 
	cr.from_c =  cm1.id AND
	cm1.topic = tm1.id AND
	tm1.section_c = 1 AND
	cr.to_c = cm2.id AND
	cm2.topic = tm2.id AND
	cr.relation = 'costudents'
