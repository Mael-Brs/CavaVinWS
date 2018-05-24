import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Color e2e test', () => {

    let navBarPage: NavBarPage;
    let colorDialogPage: ColorDialogPage;
    let colorComponentsPage: ColorComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Colors', () => {
        navBarPage.goToEntity('color');
        colorComponentsPage = new ColorComponentsPage();
        expect(colorComponentsPage.getTitle()).toMatch(/cavavinApp.color.home.title/);

    });

    it('should load create Color dialog', () => {
        colorComponentsPage.clickOnCreateButton();
        colorDialogPage = new ColorDialogPage();
        expect(colorDialogPage.getModalTitle()).toMatch(/cavavinApp.color.home.createOrEditLabel/);
        colorDialogPage.close();
    });

    it('should create and save Colors', () => {
        colorComponentsPage.clickOnCreateButton();
        colorDialogPage.setColorNameInput('colorName');
        expect(colorDialogPage.getColorNameInput()).toMatch('colorName');
        colorDialogPage.save();
        expect(colorDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ColorComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-color div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ColorDialogPage {
    modalTitle = element(by.css('h4#myColorLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    colorNameInput = element(by.css('input#field_colorName'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setColorNameInput = function (colorName) {
        this.colorNameInput.sendKeys(colorName);
    }

    getColorNameInput = function () {
        return this.colorNameInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
