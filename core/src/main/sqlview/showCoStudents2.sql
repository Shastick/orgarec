Select tm1.name, tm2.name, value from
	courserelationmap crm,
	coursemap cm1,
	coursemap cm2,
	topicmap tm1,
	topicmap tm2
	
WHERE crm.from_c = cm1.id 
	AND cm1.topic = tm1.id
	AND crm.to_c = cm2.id
	AND cm2.topic = tm1.id
	AND tm1.name like 'Crypto%'
	AND tm2.name like 'Physique%'
