package ch.epfl.craft.view.snippet

/**
 * Created with IntelliJ IDEA.
 * User: christopher
 * Date: 04.11.12
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
object SampleGraph extends GraphRepresentation{

  val n0 =  Node("0","Modeles stochastiques pour les communications)", "Mod Stoch",6)
  val n1 =  Node("1","Principles of digital communications","PDC",6)
  val n2 =  Node("2","Securité des réseaux","Securité",4)
  val n3 =  Node("3","Signal processing for communications","Signal proc.",6)
  val n4 =  Node("4","Compiler construction","Compiler",6)
  val n5 =  Node("5","Electromagnétisme I : lignes et ondes","EM 1",3)
  val n6 =  Node("6","Electromagnétisme II : calcul des champs","EM 2",3)
  val n7 =  Node("7","Electronique II","Elec 2",4)
  val n8 =  Node("8","Electronique III","Elec 3",3)
  val n9 =  Node("9","Functional materials in communication systems","FMCS",3)
  val n10 = Node("10","Graph theory applications","GTA",4)
  val n11 = Node("11","Informatique du temps réel","ITR",4)
  val n12 = Node("12","Intelligence artificielle","IA",4)
  val n13 = Node("13","Internet analytics","Internet",5)
  val n14 = Node("14","Introduction to computer graphics","Comp Graph",6)
  val n15 = Node("15","Introduction to database systems","DB",4)
  val n16 = Node("16","Operating systems","OS",4)
  val n17 = Node("17","Ressources humaines dans les projets","RES",2)
  val n18 = Node("18","Software development project","SDP",4)
  val n19 = Node("19","Software engineering","Sweng",6)


  val nodes = List(n0,n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19)


  val links = List(
    Link("0","1", 10),
    Link("0","2", 3),
    Link("0","3",8),
    Link("0","10", 5),
    Link("1","2",3),
    Link("1","3",6),
    Link("1","5",1),
    Link("2","3",5),
    Link("2","13",5),
    Link("4","19",5),
    Link("5","6",10),
    Link("5","9",4),
    Link("7","8",10),
    Link("8","9",3),
    Link("14","19",10),
    Link("18","19",7),
    Link("0", "19", 7, showLink=false)
  )

  val graph = Graph(nodes, links)

}
