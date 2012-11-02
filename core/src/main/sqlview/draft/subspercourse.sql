-- Show how many students where subscribed to a course
CREATE OR REPLACE VIEW subspercourse AS 
 SELECT s1.course, count(s1.course) AS count
   FROM subscribed s1
  GROUP BY s1.course;
