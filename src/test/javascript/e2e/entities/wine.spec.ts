import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Wine e2e test', () => {

    let navBarPage: NavBarPage;
    let wineDialogPage: WineDialogPage;
    let wineComponentsPage: WineComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Wines', () => {
        navBarPage.goToEntity('wine');
        wineComponentsPage = new WineComponentsPage();
        expect(wineComponentsPage.getTitle()).toMatch(/cavavinApp.wine.home.title/);

    });

    it('should load create Wine dialog', () => {
        wineComponentsPage.clickOnCreateButton();
        wineDialogPage = new WineDialogPage();
        expect(wineDialogPage.getModalTitle()).toMatch(/cavavinApp.wine.home.createOrEditLabel/);
        wineDialogPage.close();
    });

   /* it('should create and save Wines', () => {
        wineComponentsPage.clickOnCreateButton();
        wineDialogPage.setNameInput('name');
        expect(wineDialogPage.getNameInput()).toMatch('name');
        wineDialogPage.setAppellationInput('appellation');
        expect(wineDialogPage.getAppellationInput()).toMatch('appellation');
        wineDialogPage.setProducerInput('producer');
        expect(wineDialogPage.getProducerInput()).toMatch('producer');

        wineDialogPage.regionSelectLastOption();
        wineDialogPage.colorSelectLastOption();
        wineDialogPage.save();
        expect(wineDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); */

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class WineComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-wine div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class WineDialogPage {
    modalTitle = element(by.css('h4#myWineLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    appellationInput = element(by.css('input#field_appellation'));
    producerInput = element(by.css('input#field_producer'));
    regionSelect = element(by.css('select#field_region'));
    colorSelect = element(by.css('select#field_color'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function (name) {
        this.nameInput.sendKeys(name);
    }

    getNameInput = function () {
        return this.nameInput.getAttribute('value');
    }

    setAppellationInput = function (appellation) {
        this.appellationInput.sendKeys(appellation);
    }

    getAppellationInput = function () {
        return this.appellationInput.getAttribute('value');
    }

    setProducerInput = function (producer) {
        this.producerInput.sendKeys(producer);
    }

    getProducerInput = function () {
        return this.producerInput.getAttribute('value');
    }

    regionSelectLastOption = function () {
        this.regionSelect.all(by.tagName('option')).last().click();
    }

    regionSelectOption = function (option) {
        this.regionSelect.sendKeys(option);
    }

    getRegionSelect = function () {
        return this.regionSelect;
    }

    getRegionSelectedOption = function () {
        return this.regionSelect.element(by.css('option:checked')).getText();
    }

    colorSelectLastOption = function () {
        this.colorSelect.all(by.tagName('option')).last().click();
    }

    colorSelectOption = function (option) {
        this.colorSelect.sendKeys(option);
    }

    getColorSelect = function () {
        return this.colorSelect;
    }

    getColorSelectedOption = function () {
        return this.colorSelect.element(by.css('option:checked')).getText();
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
