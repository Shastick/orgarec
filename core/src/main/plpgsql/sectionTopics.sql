-- return the topics teached by the specified sections
-- usage: sectionTopics('{IN,SC}'::varchar[])
CREATE OR REPLACE FUNCTION sectionTopics(
	IN sname character varying[],
	OUT t_name character varying,
	OUT t_id long,
	OUT s_name character varying
)
RETURNS SETOF record AS
$$
DECLARE
	retval RECORD;
BEGIN
  FOR retval IN
	SELECT
		tm.name as tname, tm.id as tid, sm.name as sname
	FROM
		topicmap tm,
		sectionmap sm
	WHERE 
		tm.section_c = sm.id
		AND sm.name ilike any(sname)
  LOOP
	t_name:= retval.tname;
	t_id:= retval.tid;
	s_name:= retval.sname;
	RETURN NEXT;
  END LOOP;
  RETURN;
 END;
 $$
 LANGUAGE plpgsql;
	
