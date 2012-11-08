-- return the topics teached by the specified sections
-- usage: sectionTopics('{IN,SC}'::varchar[])
CREATE OR REPLACE FUNCTION sectionTopics(
	IN i_sname character varying[],
	OUT t_name character varying,
	OUT t_isa_id character varying,
	OUT s_name character varying,
	OUT t_creds integer,
	OUT t_descr character varying
)
RETURNS SETOF record AS
$$
DECLARE
	retval RECORD;
BEGIN
  FOR retval IN
	SELECT
		tm.name as tname, tm.isa_id as tid, sm.name as sname, tm.credits as creds, tm.description as descr
	FROM
		topicmap tm,
		sectionmap sm
	WHERE 
		tm.section_c = sm.id
		AND sm.name ilike any(i_sname)
  LOOP
	t_name:= retval.tname;
	t_isa_id:= retval.tid;
	s_name:= retval.sname;
	t_creds:= retval.creds;
	t_descr:= retval.descr;
	RETURN NEXT;
  END LOOP;
  RETURN;
 END;
 $$
 LANGUAGE plpgsql;
	
