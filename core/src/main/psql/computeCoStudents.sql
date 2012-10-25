INSERT INTO courserelationmap("value", to_c, from_c, relation)
SELECT count,c2id,c1id,'costudents'
FROM coursecostudentscount;
SELECT 
	count(s1.student),
	s2.course,
	s1.course, 
	'costudents'
   FROM subscribed s1,
	subscribed s2

  WHERE s1.course < s2.course
	AND s1.student = s2.student
	
  GROUP BY s1.course,
	s2.course
