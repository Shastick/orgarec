SELECT 
	s1.course c1id,
	tm1.name,
	tm1.id,
	s2.course c2id, 
	tm2.name,
	tm2.id,
	count(s1.student) count
   FROM 
	subscribed s1,
	subscribed s2,
	coursemap cm1,
	coursemap cm2,
	topicmap tm1,
	topicmap tm2
  WHERE s1.course < s2.course AND s1.student = s2.student
	AND s1.course = cm1.id
	AND cm1.topic = tm1.id
	AND s2.course = cm2.id
	AND cm2.topic = tm2.id
	
  GROUP BY s1.course, s2.course, tm1.name, tm2.name, tm1.id, tm2.id;
