/**
 * 	Copyright (C) 2011 Sam Macbeth <sm1106 [at] imperial [dot] ac [dot] uk>
 *
 * 	This file is part of Presage2.
 *
 *     Presage2 is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Presage2 is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser Public License
 *     along with Presage2.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.imperial.presage2.db.graph;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import uk.ac.imperial.presage2.core.db.DatabaseService;
import uk.ac.imperial.presage2.core.db.GraphDB;
import uk.ac.imperial.presage2.core.db.persistent.PersistentSimulation;
import uk.ac.imperial.presage2.core.db.persistent.SimulationFactory;

class Neo4jDatabase implements DatabaseService, GraphDB {

	enum SubRefs implements RelationshipType {
		SIMULATIONS, SIMULATION_STATES, SIMULATION_PARAMETERS, SIMULATION_TIMESTEPS, PLUGINS, AGENTS
	}
	
	protected final Logger logger = Logger.getLogger(Neo4jDatabase.class);

	GraphDatabaseService graphDB = null;
	
	SimulationFactory simFactory;
	
	PersistentSimulation simulation;

	private static String databasePath = "var/presagedb";

	@Override
	public void start() throws Exception {
		if (graphDB == null) {
			logger.info("Starting embedded Neo4j database at " + databasePath);
			graphDB = new EmbeddedGraphDatabase(databasePath);

			simFactory = new SimulationNode.Factory(graphDB);
		}
	}

	@Override
	public boolean isStarted() {
		return graphDB != null;
	}

	@Override
	public void stop() {
		if (graphDB != null) {
			logger.info("Shutting down Neo4j database...");
			graphDB.shutdown();
		}
	}

	@Override
	public SimulationFactory getSimulationFactory() {
		return simFactory;
	}

	@Override
	public PersistentSimulation getSimulation() {
		return simulation;
	}

	@Override
	public void setSimulation(PersistentSimulation sim) {
		simulation = sim;
	}
	
	static Node getSubRefNode(GraphDatabaseService db, SubRefs type) {
		Relationship r = db.getReferenceNode().getSingleRelationship(type,
				Direction.OUTGOING);
		if (r == null) {
			Transaction tx = db.beginTx();
			try {
				Node subRef = db.createNode();
				r = db.getReferenceNode().createRelationshipTo(subRef, type);
				tx.success();
			} finally {
				tx.finish();
			}
		}
		return r.getEndNode();
	}

}
