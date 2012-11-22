-- get details about section participation in each topic given by a section. 
-- usage : select sectionPerTopicDetail('{SHS}'::varchar[],'2012-07-01','2012-07-01')
-- Returns the topic isa_id and name, the section participating in this topic (NOT the one teaching it),
-- the average participating students from the section and the total number of students in this topic.
-- these two last numbers are averaged over the number of courses that were considered in the query
-- (if a span greater that 1 semester is provided, several occurences of the same topic will be counted.) 

CREATE OR REPLACE FUNCTION sectionPerTopicDetail(
        IN in_secname character varying[],
        IN from_sem timestamp without time zone,
        IN to_sem timestamp without time zone,
        OUT o_isa_id character varying,
        OUT o_name character varying,
        OUT o_section character varying,
        OUT o_sec_avg float,
        OUT o_topic_avg float,
        OUT o_item_count integer
)
RETURNS SETOF record AS
$$
DECLARE
        retval RECORD;
BEGIN
  FOR retval IN
	SELECT studPerSec.isa_id, tname, cname, scount/COUNT(topicTotStuds.cid) as secavg,
		SUM(topicTotStuds.count)/COUNT(topicTotStuds.cid) as totavg,
		COUNT(topicTotStuds.cid) items
	FROM
	(SELECT tm.id as tid, tm.isa_id, tm.name as tname, scm2.name as cname, COUNT(s.id) as scount
	FROM	
		coursemap cm,
		topicmap tm,
		subscribed s,
		studentmap sm,
		sectionmap scm1,
		sectionmap scm2,
		semestermap sem
	WHERE	
		cm.topic = tm.id
		AND tm.section_c = scm1.id
		AND scm1.name ilike any(in_secname)
		AND s.course = cm.id
		AND s.student = sm.id
		AND sm.section_c = scm2.id
		AND cm.semester = sem.id

		AND sem.year >= from_sem
		AND sem.year <= to_sem

	GROUP BY
		tm.id, tm.isa_id, tm.name, scm2.name
		) AS studPerSec,
	(SELECT tm.id as tid,
                cm.id as cid,
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
                AND sm.name ilike any(in_secname)
        GROUP BY
                tid, cid
	) 	AS topicTotStuds
        WHERE studPerSec.tid = topicTotStuds.tid
        GROUP BY
		studPerSec.isa_id, tname, cname, scount
	ORDER BY
		studPerSec.isa_id
  LOOP
	o_isa_id:= retval.isa_id ;
	o_name:= retval.tname ;
	o_section:= retval.cname;
        o_sec_avg:= retval.secavg;
        o_topic_avg:= retval.totavg;
        o_item_count:= retval.items;
	RETURN NEXT;
  END LOOP;
  RETURN;
END;
$$
language plpgsql
