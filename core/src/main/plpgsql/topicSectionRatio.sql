-- return the sections present in this course and the corresponding ratio.
-- the ratio is averaged over the number of courses that occured
-- in th given period.
CREATE OR REPLACE FUNCTION topicSectionRatio(
	IN in_isa_id character varying,
	IN from_sem timestamp without time zone,
	IN to_sem timestamp without time zone,
	OUT o_secname character varying,
	OUT o_avg float,
	OUT o_courseqtt integer
)
RETURNS SETOF record AS
$$
DECLARE
	retval RECORD;
BEGIN
  FOR retval IN
	select secname, SUM(count)/COUNT(cid) as avg, COUNT(cid) as c_count
FROM
	(SELECT 	secm.name as secname, cm.id cid, COUNT(s.student)
		FROM	topicmap tm,
			coursemap cm,
			semestermap sm,
			subscribed s,
			studentmap stm,
			sectionmap secm
			
		WHERE	tm.isa_id = in_isa_id
			AND cm.topic = tm.id
			AND cm.semester = sm.id
			AND s.course = cm.id
			AND s.student = stm.id
			AND stm.section_c = secm.id

			AND sm.year >= from_sem
			AND sm.year <= to_sem
			
		GROUP BY 
			tm.name, secm.name, cm.id) subtot
	GROUP BY
		secname
  LOOP
	o_secname := retval.secname;
	o_avg := retval.avg;
	o_courseqtt := retval.c_count;
	RETURN NEXT;
  END LOOP;
  RETURN;
END;
$$
LANGUAGE plpgsql
