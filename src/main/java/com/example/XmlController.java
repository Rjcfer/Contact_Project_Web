package com.example;

import com.example.accessingdatajpa.Contact;
import com.example.accessingdatajpa.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/xml")
public class XmlController {

	@Autowired
	private ContactRepository repository;

	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> handleXmlRequest(
			@RequestParam String action,
			@RequestParam(required = false) Long id) {

		try {
			switch (action.toLowerCase()) {
				case "listcontacts":
					return ResponseEntity.ok(listContactsXml());

				case "getcontact":
					if (id == null) {
						return ResponseEntity.badRequest()
								.body("<error>ID parameter is required for getContact action</error>");
					}
					return getContactXml(id);

				case "delcontact":
					if (id == null) {
						return ResponseEntity.badRequest()
								.body("<error>ID parameter is required for delContact action</error>");
					}
					return deleteContactXml(id);

				default:
					return ResponseEntity.badRequest()
							.body("<error>Unknown action: " + action + "</error>");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body("<error>Internal server error: " + e.getMessage() + "</error>");
		}
	}

	@PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> createOrUpdateContact(@RequestBody String xmlContent) {
		try {
			Contact contact = parseContactFromXml(xmlContent);
			Contact savedContact = repository.save(contact);

			return ResponseEntity.ok(
					"<response>" +
							"<status>success</status>" +
							"<message>Contact " + (contact.getId() == null ? "created" : "updated") + " successfully</message>" +
							"<contact>" + contactToXml(savedContact) + "</contact>" +
							"</response>"
			);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("<error>Failed to parse XML or save contact: " + e.getMessage() + "</error>");
		}
	}

	private String listContactsXml() {
		List<Contact> contacts = repository.findAll();
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("<contacts>");

		for (Contact contact : contacts) {
			xml.append(contactToXml(contact));
		}

		xml.append("</contacts>");
		return xml.toString();
	}

	private ResponseEntity<String> getContactXml(Long id) {
		Optional<Contact> contactOpt = repository.findById(id);

		if (contactOpt.isPresent()) {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<contact>" + contactToXml(contactOpt.get()) + "</contact>";
			return ResponseEntity.ok(xml);
		} else {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<contact> Contact id:" + id + " not found </contact>";
			return ResponseEntity.ok(xml);

		}
	}

	private ResponseEntity<String> deleteContactXml(Long id) {
		if (repository.existsById(id)) {
			repository.deleteById(id);
			return ResponseEntity.ok(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
							"<response>" +
							"<status>success</status>" +
							"<message>Contact with ID " + id + " deleted successfully</message>" +
							"</response>"
			);
		} else {
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
					"<contact> Contact id:" + id + " not found </contact>";
			return ResponseEntity.ok(xml);
		}
	}

	private String contactToXml(Contact contact) {
		return "<contact>" +
				"<id>" + contact.getId() + "</id>" +
				"<firstName>" + escapeXml(contact.getFirstName()) + "</firstName>" +
				"<lastName>" + escapeXml(contact.getLastName()) + "</lastName>" +
				"</contact>";
	}

	private Contact parseContactFromXml(String xmlContent) {
		Contact contact = new Contact();
		if (xmlContent.contains("<id>")) {
			String idStr = extractXmlValue(xmlContent, "id");
			if (!idStr.isEmpty()) {
				System.out.println("No id");
			}
		}

		contact.setFirstName(extractXmlValue(xmlContent, "firstName"));
		contact.setLastName(extractXmlValue(xmlContent, "lastName"));

		return contact;
	}

	private String extractXmlValue(String xml, String tagName) {
		String startTag = "<" + tagName + ">";
		String endTag = "</" + tagName + ">";

		int startIndex = xml.indexOf(startTag);
		int endIndex = xml.indexOf(endTag);

		if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
			return xml.substring(startIndex + startTag.length(), endIndex).trim();
		}

		return "";
	}

	private String escapeXml(String text) {
		if (text == null) return "";
		return text.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}
}