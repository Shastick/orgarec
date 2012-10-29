select SUM(crm.value), tm1.name, tm1.id, tm2.name, tm2.id
from
	courserelationmap crm,
	coursemap cm1,
	coursemap cm2,
	topicmap tm1,
	topicmap tm2
where
	crm.relation = 'costudents'
	and crm.from_c = cm1.id
	and cm1.topic = tm1.id
	and crm.to_c = cm2.id
	and cm2.topic = tm2.id
group by
	tm1.name, tm1.id, tm2.name, tm2.id
