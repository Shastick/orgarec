-- return the topics teached by the specified sections
-- usage: sectionTopics('{IN,SC}'::varchar[],'2011-07-01','2012-07-01')
CREATE OR REPLACE FUNCTION sectionTopicsWStudentCount(
	IN i_sname character varying[],
	IN from_sem timestamp without time zone,
        IN to_sem timestamp without time zone,
	OUT t_name character varying,
	OUT t_isa_id character varying,
	OUT s_name character varying,
	OUT t_creds integer,
	OUT t_descr character varying,
	OUT t_savg float,
	OUT t_ccount integer
)
RETURNS SETOF record AS
$$
DECLARE
	retval RECORD;
BEGIN
  FOR retval IN
	SELECT 	tname, tid, sname,
		creds, descr, SUM(count)/COUNT(cid) as avg,
		COUNT(cid) as ccount
	FROM 
	(SELECT
		tm.name as tname,
		tm.isa_id as tid,
		cm.id as cid,
		sm.name as sname,
		tm.credits as creds,
		tm.description as descr,
		COUNT(s.student) as count
	FROM
		topicmap tm,
		sectionmap sm,
		coursemap cm,
		subscribed s,
		semestermap smm
	WHERE 
		tm.section_c = sm.id
		AND cm.topic = tm.id
		AND s.course = cm.id
		AND cm.semester = smm.id
		AND smm.year >= from_sem
		AND smm.year <= to_sem
		AND sm.name ilike any(i_sname)
	GROUP BY
		tname, tid, cid, sname, creds, descr
	) AS per_course
	GROUP BY tname, tid, sname, creds, descr
  LOOP
	t_name:= retval.tname;
	t_isa_id:= retval.tid;
	s_name:= retval.sname;
	t_creds:= retval.creds;
	t_descr:= retval.descr;
	t_savg:= retval.avg;
	t_ccount:= retval.ccount;
	RETURN NEXT;
  END LOOP;
  RETURN;
 END;
$$

LANGUAGE plpgsql
