  SELECT
        count(s1.student) as sum,
        s2.course as c1,
        s1.course as c2

   FROM subscribed s1,
        subscribed s2

  WHERE s1.course < s2.course
        AND s1.student = s2.student

  GROUP BY s1.course,
        s2.course
