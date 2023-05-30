package es.urjc.code.daw.library.book;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.urjc.code.daw.library.notification.NotificationService;

/* Este servicio se usará para incluir la funcionalidad que sea 
 * usada desde el BookRestController y el BookWebController
 */
@Service
public class BookService {

	private BookRepository repository;
	private NotificationService notificationService;
	private LineBreaker lineBreaker;

	public BookService(BookRepository repository, NotificationService notificationService, LineBreaker lineBreaker) {
		this.repository = repository;
		this.notificationService = notificationService;
		this.lineBreaker = lineBreaker;
	}

	public Optional<Book> findOne(long id) {
		return repository.findById(id);
	}
	
	public boolean exist(long id) {
		return repository.existsById(id);
	}

	public List<Book> findAll() {
		return repository.findAll();
	}

	public Book save(Book book) {
		Book bookWithLinesBreaked = new Book(book.getTitle(), lineBreaker.breakLine(book.getDescription(), 10));
		Book newBook = repository.save(bookWithLinesBreaked);
		notificationService.notify("Book Event: book with title="+newBook.getTitle()+" was created");
		return newBook;
	}

	public void delete(long id) {
		repository.deleteById(id);
		notificationService.notify("Book Event: book with id="+id+" was deleted");
	}
}
