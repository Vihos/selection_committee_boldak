package by.grsu.boldak.controller;

import by.grsu.boldak.Starter;
import by.grsu.boldak.model.User;
import by.grsu.boldak.repository.FacultyRepository;
import by.grsu.boldak.service.UserService;
import by.grsu.boldak.DataTransferObject.UserRegistrationDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
	private static final Logger logger = Logger.getLogger(UserRegistrationController.class);

	@Autowired // This means to get the bean called userRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private FacultyRepository facultyRepository;

	@Autowired
	private UserService userService;

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping
	public String showRegistrationForm(Model model) {
		model.addAttribute("facultys", facultyRepository.findAll());

		return "registration";
	}

	@PostMapping
	public String registerUserAccount(Model model, @ModelAttribute("user") @Valid UserRegistrationDto userDto,
									  BindingResult result) {

		model.addAttribute("facultys", facultyRepository.findAll());

		User existing = userService.findByEmail(userDto.getEmail());
		if (existing != null) {
			result.rejectValue("email", null, "Аккаунт с таким e-mail адресом уже зарегистрирован");
		}

		if(facultyRepository.findById(userDto.getFaculty()) == null) {
			result.rejectValue("faculty", null, "Неверно заполнено поле");
		}

		if (result.hasErrors()) {
			return "registration";
		}

		userService.save(userDto);
		return "redirect:/registration?success";
	}

}
