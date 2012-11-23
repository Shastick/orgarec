-- Show which courses where taken during what academic level and the section giving the course.
SELECT DISTINCT
		sub.course AS course,
		asm.level_c AS level_c,
		sm.name AS section
	FROM
		subscribed sub,
		academicsemestermap asm,
		coursemap cm,
		topicmap tm,
		sectionmap sm
	WHERE 	
		sub.academicsemester = asm.id
		AND sub.course = cm.id
		AND cm.topic = tm.id
		AND tm.section_c = sm.id
