-- Show costudents quantity between any two courses.
CREATE OR REPLACE VIEW coursecostudentscount AS 
 SELECT s1.course AS c1id, s2.course AS c2id, count(s1.student) AS count
   FROM subscribed s1, subscribed s2
  WHERE s1.course < s2.course AND s1.student = s2.student
  GROUP BY s1.course, s2.course;
