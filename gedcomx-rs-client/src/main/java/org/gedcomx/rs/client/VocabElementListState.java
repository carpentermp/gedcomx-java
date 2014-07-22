/**
 * Copyright Intellectual Reserve, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gedcomx.rs.client;

import com.github.jsonldjava.jena.JenaJSONLD;
import com.hp.hpl.jena.rdf.model.*;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import org.apache.jena.riot.RDFDataMgr;
import org.gedcomx.common.URI;
import org.gedcomx.links.SupportsLinks;
import org.gedcomx.rs.Rel;
import org.gedcomx.rs.client.util.VocabConstants;
import org.gedcomx.vocab.VocabElement;
import org.gedcomx.vocab.VocabElementList;

public class VocabElementListState extends GedcomxApplicationState<Model> {

  private Resource resourceDescribingList;

  static {
    JenaJSONLD.init();
  }

  protected VocabElementListState(ClientRequest request, ClientResponse response, String accessToken, StateFactory stateFactory) {
    super(request, response, accessToken, stateFactory);
  }

  @Override
  public String getSelfRel() {
    return Rel.DESCRIPTION;
  }

  @Override
  protected VocabElementState clone(ClientRequest request, ClientResponse response) {
    return new VocabElementState(request, response, this.accessToken, this.stateFactory);
  }

  @Override
  public VocabElementState ifSuccessful() {
    return (VocabElementState) super.ifSuccessful();
  }

  @Override
  public VocabElementState head(StateTransitionOption... options) {
    return (VocabElementState) super.head(options);
  }

  @Override
  public VocabElementState options(StateTransitionOption... options) {
    return (VocabElementState) super.options(options);
  }

  @Override
  public VocabElementState get(StateTransitionOption... options) {
    return (VocabElementState) super.get(options);
  }

  @Override
  public VocabElementState delete(StateTransitionOption... options) {
    return (VocabElementState) super.delete(options);
  }

  @Override
  public VocabElementState put(Model e, StateTransitionOption... options) {
    return (VocabElementState) super.put(e, options);
  }

  @Override
  public VocabElementState post(Model entity, StateTransitionOption... options) {
    return (VocabElementState) super.post(entity, options);
  }

  public VocabElementList getVocabElementList() {

    Model model = resourceDescribingList.getModel();
    // Create and populate the vocabulary element list
    VocabElementList vocabElementList = new VocabElementList();
    Property identifierProperty = model.createProperty(VocabConstants.DC_NAMESPACE, "identifier");
    if (resourceDescribingList.hasProperty(identifierProperty)) {
      vocabElementList.setId(resourceDescribingList.getRequiredProperty(model.createProperty(VocabConstants.DC_NAMESPACE, "identifier")).getString());
    }
    vocabElementList.setUri(URI.create(resourceDescribingList.getURI()));
    vocabElementList.setTitle(resourceDescribingList.getRequiredProperty(model.createProperty(VocabConstants.DC_NAMESPACE, "title")).getString());
    vocabElementList.setDescription(resourceDescribingList.getRequiredProperty(model.createProperty(VocabConstants.DC_NAMESPACE, "description")).getString());

    // Populate the list of vocabulary elements within the vocabulary element list
    Seq seq = resourceDescribingList.as(Seq.class);
    for (int i = 0; i < seq.size(); i++) {
      vocabElementList.addElement(mapToVocabElement(seq.getResource(i + 1)));
    }

    return vocabElementList;
  }

  @Override
  protected Model loadEntity(ClientResponse response) {
    Model model = ModelFactory.createDefaultModel();
    RDFDataMgr.read(model, response.getEntityInputStream(), null, JenaJSONLD.JSONLD);
    this.resourceDescribingList = model.getResource(this.request.getURI().toString());

    return model;
  }

  @Override
  protected SupportsLinks getMainDataElement() {
//    return getPlaceDescription();
    return null;
  }

  /**
   * Map a RDF resource that represents a vocabulary element to a GedcomX vocabulary element
   *
   * @param resourceDescribingElement the RDF resource that represents a vocabulary element
   * @return a GedcomX vocabulary element that corresponds to the given RDF resource
   */
  private VocabElement mapToVocabElement(Resource resourceDescribingElement) {

    VocabElement vocabElement = new VocabElement();
    Model model = resourceDescribingElement.getModel();

    // Map required attributes into the VocabElement
    vocabElement.setId(resourceDescribingElement.getRequiredProperty(model.createProperty(VocabConstants.DC_NAMESPACE, "identifier")).getString());
    vocabElement.setUri(URI.create(resourceDescribingElement.getURI()));

    // Get optional attributes into the VocabElement
    Property property = model.createProperty(VocabConstants.RDFS_NAMESPACE, "subClassOf");
    if (resourceDescribingElement.hasProperty(property)) {
      vocabElement.setSubclass(URI.create(resourceDescribingElement.getRequiredProperty(model.createProperty(VocabConstants.RDFS_NAMESPACE, "subClassOf")).getResource().getURI()));
    }
    property = model.createProperty(VocabConstants.DC_NAMESPACE, "type");
    if (resourceDescribingElement.hasProperty(property)) {
      vocabElement.setType(URI.create(resourceDescribingElement.getRequiredProperty(model.createProperty(VocabConstants.DC_NAMESPACE, "type")).getResource().getURI()));
    }

    // Map the labels into the VocabElement
    StmtIterator labels = resourceDescribingElement.listProperties(model.createProperty(VocabConstants.RDFS_NAMESPACE, "label"));
    while (labels.hasNext()) {
      Statement next = labels.next();
      vocabElement.addLabel(next.getString(), next.getLanguage().toLowerCase());
    }

    // Map the descriptions into the VocabElement
    StmtIterator descriptions = resourceDescribingElement.listProperties(model.createProperty(VocabConstants.RDFS_NAMESPACE, "comment"));
    while (descriptions.hasNext()) {
      Statement next = descriptions.next();
      vocabElement.addDescription(next.getString(), next.getLanguage().toLowerCase());
    }
    return vocabElement;
  }
}
