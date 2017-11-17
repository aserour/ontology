/** Copyright (C) Cit Global, Inc (VERICASH) - All Rights Reserved
 * Unauthorized copying or modification of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * 
 * http://citvericash.com
 * 
 */
package com.fatema.tdb;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 
 * @since Nov 17, 2017
 * @module ontology-service
 * @email 
 */
@Configuration
public class TDBConnection {

	@Bean
	public String connecct() {
		// Make a TDB-backed dataset
		  String directory = System.getProperty("user.home")+"/Data/Private/Work/fatema/context-tdb" ;
		  Dataset dataset = TDBFactory.createDataset(directory) ;
		  
		  dataset.begin(ReadWrite.READ) ;
		  // Get model inside the transaction
		  Model model = dataset.getDefaultModel() ;
		  
		  
		  // select
		  
		  String queryString = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
		  		"prefix owl: <http://www.w3.org/2002/07/owl#>\n" + 
		  		"prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" + 
		  		"prefix family: <http://www.owl-ontologies.com/generations.owl#>\n" + 
		  		"\n" + 
		  		"SELECT DISTINCT  ?parent #?son ?hasParent ?parent\n" + 
		  		"WHERE {\n" + 
		  		"#  ?son   a	  family:Person.\n" + 
		  		"#  ?son	family:hasParent	?parent.\n" + 
		  		"\n" + 
		  		"  	?parentNode		a						family:Person.\n" + 
		  		"  	?parentNode		family:fullName			?parent.\n" + 
		  		"	?sonNode		family:hasParent		?parentNode.\n" + 
		  		"}\n" + 
		  		"LIMIT 25 " ;
		  
		  Query query = QueryFactory.create(queryString) ;
		  try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
//		      RDFNode x = soln.get("varName") ;       // Get a result variable by name.
//		      Resource r = soln.getResource("VarR") ; // Get a result variable - must be a resource
		      Literal l = soln.getLiteral("parent") ;   // Get a result variable - must be a literal
		      
		      System.out.println("Result: "+ l);
		      
		    }
		  }
		  
		  
		  //close model
		  dataset.end() ;
		  
//		  dataset.begin(ReadWrite.WRITE) ;
//		  model = dataset.getDefaultModel() ;
//		  dataset.end() ;
		  return "";
	}
	
}
