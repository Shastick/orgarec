-- get the costudents quantity between two topics
-- usage : select topicCostudents('{SC,IN}'::varchar[],'{BA1}'::varchar[],NULL,NULL)
CREATE OR REPLACE FUNCTION topicCostudents(
	IN sname character varying[],
	IN from_sem timestamp without time zone,
	IN to_sem timestamp without time zone,
	OUT t_isa_id1 character varying,
	OUT t_isa_id2 character varying,
	OUT t_sum bigint
	)
RETURNS SETOF record AS
$$

DECLARE
  retval RECORD;
BEGIN
  CREATE TEMP TABLE topic_costudents
	ON COMMIT DROP
	AS
	SELECT 
		sum(crm.value) AS sum,
		tm1.name AS name1,
		tm1.isa_id AS id1,
		tm2.name AS name2,
		tm2.isa_id AS id2
	FROM 
		courserelationmap crm,
		coursemap cm1,
		coursemap cm2,
		topicmap tm1,
		topicmap tm2,
		sectionmap sm1,
		sectionmap sm2,
		semestermap smm1,
		semestermap smm2,
		academicsemestermap asm1,
		academicsemestermap asm2,
		subscribed sub1,
		subscribed sub2
	WHERE 
		crm.relation::text = 'costudents'::text
		AND crm.from_c = cm1.id 
		AND cm1.topic = tm1.id 
		AND crm.to_c = cm2.id 
		AND cm2.topic = tm2.id
		AND tm1.section_c = sm1.id
		AND tm2.section_c = sm2.id
		AND cm1.semester = smm1.id
		AND cm2.semester = smm2.id

		AND sub1.course = cm1.id
		AND sub2.course = cm2.id
		AND sub1.academicsemester = asm1.id
		AND sub2.academicsemester = asm2.id
		
		AND smm2.year >= from_sem
		AND smm2.year <= to_sem
		AND smm1.year >= from_sem
		AND smm2.year <= to_sem
		AND sm1.name ilike any(sname)
		AND sm2.name ilike any(sname)
		AND asm1.level_c ilike any(acad_sem)
		AND asm2.level_c ilike any(acad_sem)
	GROUP BY 
		tm1.name,
		tm1.isa_id,
		tm2.name,
		tm2.isa_id
;
  FOR retval IN	
	SELECT 
		tc1.name1,
		tc1.id1,
		tc1.name2,
		tc1.id2,
		(tc1.sum + COALESCE(tc2.sum,0)) as sum
	FROM 
		topic_costudents tc1
		LEFT JOIN
		topic_costudents tc2	

	ON 
		tc1.id1 = tc2.id2
		AND tc2.id1 = tc1.id2
		AND tc1.id2 < tc1.id1
  LOOP
    t_isa_id1 := retval.id1;
    t_isa_id2 := retval.id2;
    t_sum := retval.sum;
    RETURN NEXT;
  END LOOP;
  RETURN;
END;

$$
LANGUAGE plpgsql;
