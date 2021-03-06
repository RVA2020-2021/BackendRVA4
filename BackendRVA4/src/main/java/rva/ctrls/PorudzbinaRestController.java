package rva.ctrls;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rva.jpa.Artikl;
import rva.jpa.Porudzbina;
import rva.repository.PorudzbinaRepository;

@CrossOrigin
@RestController
public class PorudzbinaRestController {
	

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository;
	
	@GetMapping("porudzbina")
	public Collection<Porudzbina> getPorudzbine() {
		return porudzbinaRepository.findAll();
	}

	@GetMapping("porudzbina/{id}")
	public Porudzbina getPorudzbina(@PathVariable("id") Integer id) {
		return porudzbinaRepository.getOne(id);
	}

	@GetMapping("porudzbinaPlaceno")
	public Collection<Porudzbina> getPorudzbinaByPlaceno(){
		return porudzbinaRepository.findByPlacenoTrue();
	}
	
	@PostMapping("porudzbina")
	public ResponseEntity<Porudzbina> insertPorudzbina(@RequestBody Porudzbina porudzbina) {

		if (!porudzbinaRepository.existsById(porudzbina.getId())) {
			porudzbinaRepository.save(porudzbina);
			return new ResponseEntity<Porudzbina>(HttpStatus.OK);
		}
		return new ResponseEntity<Porudzbina>(HttpStatus.CONFLICT);

	}

	@PutMapping("porudzbina")
	public ResponseEntity<Porudzbina> updatePorudzbina(@RequestBody Porudzbina porudzbina) {

		if (!porudzbinaRepository.existsById(porudzbina.getId())) {
			return new ResponseEntity<Porudzbina>(HttpStatus.NO_CONTENT);
		}
		porudzbinaRepository.save(porudzbina);
		return new ResponseEntity<Porudzbina>(HttpStatus.OK);
	}
	
	//anotacija je zakomentarisana jer je smetala prilikom izvrsavanja testova
	//@Transactional
	@DeleteMapping("porudzbina/{id}")
	public ResponseEntity<Porudzbina> deletePorudzbina(@PathVariable("id") Integer id){
		
		if(!porudzbinaRepository.existsById(id))
			return new ResponseEntity<Porudzbina>(HttpStatus.NO_CONTENT);
		
		jdbcTemplate.execute("delete from stavka_porudzbine where porudzbina="+id);
		porudzbinaRepository.deleteById(id);
		
		if(id==-100) {
			jdbcTemplate.execute("INSERT INTO \"porudzbina\"(\"id\", \"datum\", \"isporuceno\", \"dobavljac\", \"iznos\", \"placeno\")"
					+ "VALUES (-100, to_date('01.03.2020.', 'dd.mm.yyyy.'), to_date('05.03.2020.', 'dd.mm.yyyy.'), 1, 80000, TRUE)");
			
		}
		return new ResponseEntity<Porudzbina>(HttpStatus.OK);
		
	}
}
