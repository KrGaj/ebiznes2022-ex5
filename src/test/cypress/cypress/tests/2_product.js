const server = require("./../config/config.json")
const products = require("./../fixtures/product.json")

describe("POST product", () => {
    it("Add product", () => {
        cy.request("POST", server.serverURL + "products", {
            name: products[0].name,
            description: products[0].description,
            price: products[0].price,
            category: Cypress.env("cartId"),
            available: products[0].available
        })
            .then((response) => {
                expect(response.status).to.eq(200)
                expect(response.body).to.have.property("productId")

                Cypress.env("productId", response.body.productId)
            })
    })

    it("Add duplicate product", () => {
        cy.request("POST", server.serverURL + "products", {
            name: products[0].name,
            description: products[0].description,
            price: products[0].price,
            category: Cypress.env("cartId"),
            available: products[0].available
        })
            .then((response) => {
                expect(response.status).to.eq(500)
            })
    })
})

describe("GET product", () => {
    it("Get all products", () => {
        cy.request("GET", server.serverURL + "products/all")
            .then((response) => {
                expect(response.status).to.eq(200)

                expect(response.body[0]).to.have.property("id")
                expect(response.body[0]).to.have.property("name")
                expect(response.body[0]).to.have.property("description")
                expect(response.body[0]).to.have.property("price")
                expect(response.body[0]).to.have.property("category")
                expect(response.body[0]).to.have.property("available")

                expect(response.body[0].id).to.eq(Cypress.env("productId"))
                expect(response.body[0].name).to.eq(products[0].name)
                expect(response.body[0].description).to.eq(products[0].description)
                expect(response.body[0].price).to.eq(products[0].price)
                expect(response.body[0].category).to.eq(Cypress.env("categoryId"))
                expect(response.body[0].available).to.eq(products[0].available)
            })
    })

    it("Get product by ID", () => {
        cy.request("GET", server.serverURL + "products?product_id" + Cypress.env("productId"))
            .then((response) => {
                expect(response.status).to.eq(200)

                expect(response.body).to.have.property("id")
                expect(response.body).to.have.property("name")
                expect(response.body).to.have.property("description")
                expect(response.body).to.have.property("price")
                expect(response.body).to.have.property("category")
                expect(response.body).to.have.property("available")

                expect(response.body.id).to.eq(Cypress.env("productId"))
                expect(response.body.name).to.eq(products[0].name)
                expect(response.body.description).to.eq(products[0].description)
                expect(response.body.price).to.eq(products[0].price)
                expect(response.body.category).to.eq(Cypress.env("categoryId"))
                expect(response.body.available).to.eq(products[0].available)
            })
    })
})
