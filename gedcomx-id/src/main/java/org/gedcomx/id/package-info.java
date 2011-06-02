/**
 * Copyright 2011 Intellectual Reserve, Inc.
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

/**
 * The <b>id profile</b> provides the standard mechanisms for identifying genealogical entities.
 *
 * @label ID Profile
 */
@XmlSchema(
  namespace = IdConstants.GEDCOMX_ID_NAMESPACE,
  elementFormDefault = XmlNsForm.QUALIFIED,
  xmlns = {
    @XmlNs ( prefix = IdConstants.GEDCOMX_ID_NAMESPACE_PREFIX, namespaceURI = IdConstants.GEDCOMX_ID_NAMESPACE),
    @XmlNs ( prefix = TypeConstants.GEDCOMX_TYPES_NAMESPACE_PREFIX, namespaceURI = TypeConstants.GEDCOMX_TYPES_NAMESPACE),
    @XmlNs ( prefix = "xlink", namespaceURI = "http://www.w3.org/1999/xlink")
  }

)
package org.gedcomx.id;

import org.gedcomx.types.TypeConstants;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;